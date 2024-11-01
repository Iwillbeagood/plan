package jun.money.mate.spending_plan

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.SpendingPlanRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.Income
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingPlanList
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

@HiltViewModel
internal class SpendingPlanListViewModel @Inject constructor(
    private val spendingPlanRepository: SpendingPlanRepository
) : ViewModel() {

    var dateState = MutableStateFlow<LocalDate>(LocalDate.now())
        private set

    private val _spendingPlanListState = MutableStateFlow<SpendingPlanListState>(SpendingPlanListState.Loading)
    val spendingPlanListState: StateFlow<SpendingPlanListState> = _spendingPlanListState.onStart {
        loadSpending()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SpendingPlanListState.Loading
    )

    val spendingListViewMode: StateFlow<SpendingListViewMode> = spendingPlanListState.flatMapLatest {
        flowOf(
            when (it) {
                is SpendingPlanListState.SpendingPlanListData -> it.spendingListViewMode
                else -> SpendingListViewMode.LIST
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SpendingListViewMode.LIST
    )

    private val _spendingPlanListEffect = MutableSharedFlow<SpendingPlanListEffect>()
    val spendingPlanListEffect: SharedFlow<SpendingPlanListEffect> get() = _spendingPlanListEffect.asSharedFlow()

    private fun loadSpending() {
        viewModelScope.launch {
            dateState.flatMapLatest {
                spendingPlanRepository.getSpendingPlansByMonth(it)
            }.collect {
                _spendingPlanListState.value = if (it.spendingPlans.isEmpty()) {
                    SpendingPlanListState.Empty
                } else {
                    SpendingPlanListState.SpendingPlanListData(it)
                }
            }
        }
    }

    fun changeSpendingSelected(spendingPlan: SpendingPlan) {
        val spendingListState = spendingPlanListState.value as? SpendingPlanListState.SpendingPlanListData ?: return

        viewModelScope.launch {
            _spendingPlanListState.update {
                spendingListState.copy(
                    spendingPlanList = spendingListState.spendingPlanList.copy(
                        spendingPlans = spendingListState.spendingPlanList.spendingPlans.map {
                            if (it.id == spendingPlan.id) {
                                it.copy(selected = !it.selected)
                            } else {
                                it.copy(selected = false)
                            }
                        }
                    )
                )
            }
        }
    }

    fun editSpending() {
        val spendingListState = spendingPlanListState.value as? SpendingPlanListState.SpendingPlanListData ?: return
        val selectedIncomeId = spendingListState.selectedIncomeId ?: return

        viewModelScope.launch {
            _spendingPlanListEffect.emit(SpendingPlanListEffect.EditSpendingPlan(selectedIncomeId))
        }
    }

    fun deleteSpending() {
        val spendingListState = spendingPlanListState.value as? SpendingPlanListState.SpendingPlanListData ?: return
        val selectedIncomeId = spendingListState.selectedIncomeId ?: return

        viewModelScope.launch {
            spendingPlanRepository.deleteById(selectedIncomeId)
            showSnackBar(MessageType.Message("수입이 삭제되었습니다"))
        }
    }

    fun onDateSelected(date: LocalDate) {
        dateState.update { date }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _spendingPlanListEffect.emit(SpendingPlanListEffect.ShowSnackBar(messageType))
        }
    }
}

@Stable
internal sealed interface SpendingPlanListState {

    @Immutable
    data object Loading : SpendingPlanListState

    @Immutable
    data object Empty : SpendingPlanListState

    @Immutable
    data class SpendingPlanListData(
        val spendingPlanList: SpendingPlanList,
    ) : SpendingPlanListState {

        val spendingListViewMode: SpendingListViewMode get() = if (spendingPlanList.spendingPlans.any { it.selected }) SpendingListViewMode.EDIT else SpendingListViewMode.LIST

        val selectedIncomeId get() = spendingPlanList.spendingPlans.firstOrNull { it.selected }?.id
    }
}

@Stable
internal sealed interface SpendingPlanListEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : SpendingPlanListEffect

    @Immutable
    data class EditSpendingPlan(val id: Long) : SpendingPlanListEffect
}

internal enum class SpendingListViewMode {
    LIST,
    EDIT
}