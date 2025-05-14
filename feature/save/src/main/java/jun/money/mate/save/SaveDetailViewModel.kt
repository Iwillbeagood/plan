package jun.money.mate.save

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.dataApi.database.SaveRepository
import jun.money.mate.domain.DeleteSaveUsecase
import jun.money.mate.domain.EditSaveUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavingsType
import jun.money.mate.navigation.Route
import jun.money.mate.save.contract.SaveDetailEffect
import jun.money.mate.save.contract.SaveDetailModalEffect
import jun.money.mate.ui.number.ValueState
import jun.money.mate.ui.number.ValueState.Companion.value
import jun.money.mate.utils.currency.CurrencyFormatter
import jun.money.mate.utils.flow.updateWithData
import jun.money.mate.utils.flow.withData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SaveDetailViewModel @Inject constructor(
    private val saveRepository: SaveRepository,
    private val editSaveUsecase: EditSaveUsecase,
    private val deleteSaveUsecase: DeleteSaveUsecase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id = savedStateHandle.toRoute<Route.Save.Detail>().id

    var isEdited = mutableStateOf(false)
        private set

    private val _saveDetailState = MutableStateFlow<SaveDetailState>(SaveDetailState.Loading)
    val saveDetailState: StateFlow<SaveDetailState> = _saveDetailState.onStart {
        fetchSavePlan()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SaveDetailState.Loading
    )

    private val _modalEffect = MutableStateFlow<SaveDetailModalEffect>(SaveDetailModalEffect.Hidden)
    val modalEffect: StateFlow<SaveDetailModalEffect> get() = _modalEffect

    private val _saveEffect = MutableSharedFlow<SaveDetailEffect>()
    val saveEffect: SharedFlow<SaveDetailEffect> get() = _saveEffect.asSharedFlow()

    private fun fetchSavePlan() {
        viewModelScope.launch {
            saveRepository.getSavePlan(id)
                .map {
                    SaveDetailState.Data(
                        savePlan = it,
                    )
                }.collect { state ->
                    _saveDetailState.update { state }
                }
        }
    }

    fun editSave() {
        saveDetailState.withData<SaveDetailState.Data> {
            viewModelScope.launch {
                editSaveUsecase(it.savePlan)
                showSnackBar(MessageType.Message("저축이 수정되었습니다"))
                showSaveDetailComplete()
                isEdited.value = false
            }
        }
    }

    fun deleteSaving() {
        saveDetailState.withData<SaveDetailState.Data> {
            viewModelScope.launch {
                deleteSaveUsecase(it.savePlan)
                showSnackBar(MessageType.Message("저축이 삭제되었습니다"))
                showSaveDetailComplete()
                isEdited.value = false
            }
        }
    }

    fun daySelected(day: Int) {
        setEditable()

        _saveDetailState.updateWithData<SaveDetailState, SaveDetailState.Data> {
            it.copy(
                savePlan = it.savePlan.copy(
                    day = day
                )
            )
        }
    }

    fun amountValueChange(value: ValueState) {
        setEditable()

        _saveDetailState.updateWithData<SaveDetailState, SaveDetailState.Data> {
            it.copy(
                savePlan = it.savePlan.copy(
                    amount = value.value(it.amountString).toLongOrNull() ?: 0
                )
            )
        }
    }

    fun setEditable() {
        isEdited.value = true
    }

    fun showDeleteDialog() {
        saveDetailState.withData<SaveDetailState.Data> {
            if (it.savePlan.savingsType is SavingsType.PeriodType) {
                _modalEffect.update { SaveDetailModalEffect.ShowPeriodDeleteConfirmDialog }
            } else {
                _modalEffect.update { SaveDetailModalEffect.ShowBasicDeleteConfirmDialog }
            }
        }
    }

    fun showNumberKeyboard() {
        _modalEffect.update { SaveDetailModalEffect.ShowNumberKeyboard }
    }

    fun dismiss() {
        _modalEffect.update { SaveDetailModalEffect.Hidden }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _saveEffect.emit(SaveDetailEffect.ShowSnackBar(messageType))
        }
    }

    private fun showSaveDetailComplete() {
        viewModelScope.launch {
            _saveEffect.emit(SaveDetailEffect.SaveDetailComplete)
        }
    }

}

@Stable
sealed interface SaveDetailState {

    @Immutable
    data object Loading : SaveDetailState

    @Immutable
    data class Data(
        val savePlan: SavePlan,
    ) : SaveDetailState {

        val amountString get() = if (savePlan.amount > 0) savePlan.amount.toString() else ""
        val amountWon get() = if (savePlan.amount > 0) CurrencyFormatter.formatAmountWon(savePlan.amount) else ""
    }
}