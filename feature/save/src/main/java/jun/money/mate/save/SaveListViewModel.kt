package jun.money.mate.save

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.model.etc.EditMode
import jun.money.mate.model.etc.ViewMode
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.utils.flow.updateWithData
import jun.money.mate.utils.flow.withData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
internal class SavingListViewModel @Inject constructor(
    private val saveRepository: SaveRepository
) : ViewModel() {

    private val _month = MutableStateFlow<LocalDate>(LocalDate.now())
    val month: StateFlow<LocalDate> get() = _month

    private val _savingListState = MutableStateFlow<SavingListState>(SavingListState.Loading)
    val savingListState: StateFlow<SavingListState> = _savingListState.onStart {
        loadSpending()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SavingListState.Loading
    )

    private val _savingListEffect = MutableSharedFlow<SavingListEffect>()
    val savingListEffect: SharedFlow<SavingListEffect> get() = _savingListEffect.asSharedFlow()

    private fun loadSpending() {
        viewModelScope.launch {
            saveRepository.getSavePlanListFlow().collect { savePlan ->
                _savingListState.value = SavingListState.SavingListData(savePlan)
            }
        }
    }

    fun changeSavePlanSelected(id: Long) {
        _savingListState.updateWithData<SavingListState, SavingListState.SavingListData> { uiState ->
            uiState.copy(
                savePlanList = uiState.savePlanList.copy(
                    savePlans = uiState.savePlanList.savePlans.map {
                        if (it.id == id) {
                            it.copy(selected = !it.selected)
                        } else {
                            it
                        }
                    },
                )
            )
        }
    }

    fun unselectAll() {
        _savingListState.updateWithData<SavingListState, SavingListState.SavingListData> { uiState ->
            uiState.copy(
                savePlanList = uiState.savePlanList.copy(
                    savePlans = uiState.savePlanList.savePlans.map {
                        it.copy(selected = false)
                    },
                )
            )
        }
    }

    fun executeChange(executed: Boolean, id: Long) {
        viewModelScope.launch {
            saveRepository.updateExecuteState(id, executed)
        }
    }

    fun editSave() {
        savingListState.withData<SavingListState.SavingListData> {
            val selectedId = it.selectedId ?: return
            viewModelScope.launch {
                _savingListEffect.emit(SavingListEffect.EditSpendingPlan(selectedId))
            }
        }

    }

    fun prevMonth() {
        _month.update {
            it.minusMonths(1)
        }
    }

    fun nextMonth() {
        _month.update {
            it.plusMonths(1)
        }
    }

    fun deleteSave() {
        val uiState = savingListState.value as? SavingListState.SavingListData ?: return
        val selectedId = uiState.selectedId ?: return

        viewModelScope.launch {
            saveRepository.deleteById(selectedId)
            showSnackBar(MessageType.Message("저축 계획이 삭제되었습니다"))
        }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _savingListEffect.emit(SavingListEffect.ShowSnackBar(messageType))
        }
    }
}

@Stable
internal sealed interface SavingListState {

    @Immutable
    data object Loading : SavingListState

    @Immutable
    data object Empty : SavingListState

    @Immutable
    data class SavingListData(
        val savePlanList: SavePlanList,
    ) : SavingListState {

        val editMode
            get() = savePlanList.savePlans.count { it.selected }.let {
                if (it > 1) {
                    EditMode.DELETE_ONLY
                } else if (it == 1) {
                    EditMode.EDIT
                } else {
                    EditMode.LIST
                }
            }

        val spendingListViewMode: ViewMode get() = if (savePlanList.savePlans.any { it.selected }) ViewMode.EDIT else ViewMode.LIST

        val totalString get() = savePlanList.totalString

        val goldAcornCount: Int get() = (savePlanList.total / 1_000_000).toInt()
        val acornCount: Int get() = ceil((savePlanList.total % 1_000_000).toDouble() / 100_000).toInt()

        val selectedId get() = savePlanList.savePlans.firstOrNull { it.selected }?.id
    }
}

@Stable
internal sealed interface SavingListEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : SavingListEffect

    @Immutable
    data class EditSpendingPlan(val id: Long) : SavingListEffect
}