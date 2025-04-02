package jun.money.mate.budget

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.BudgetRepository
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.etc.error.MessageType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
) : ViewModel() {

    val budgetState: StateFlow<BudgetState> = budgetRepository.getBudgetsFlow()
        .map {
            BudgetState.BudgetData(
                budgets = it,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = BudgetState.Loading
        )

    private val _consumptionModalEffect = MutableStateFlow<BudgetCostEffect>(BudgetCostEffect.Hidden)
    val consumptionModalEffect: StateFlow<BudgetCostEffect> get() = _consumptionModalEffect

    private val _budgetEffect = MutableSharedFlow<BudgetEffect>()
    val budgetEffect: SharedFlow<BudgetEffect> get() = _budgetEffect.asSharedFlow()


    fun hideModal() {
        _consumptionModalEffect.update { BudgetCostEffect.Hidden }
    }

    fun showBudgetAdd() {
        viewModelScope.launch {
            _budgetEffect.emit(BudgetEffect.NavigateToAdd)
        }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _budgetEffect.emit(BudgetEffect.ShowSnackBar(messageType))
        }
    }
}

@Stable
internal sealed interface BudgetState {

    @Immutable
    data object Loading : BudgetState

    @Immutable
    data class BudgetData(
        val budgets: List<Budget>,
    ) : BudgetState {

        val totalBudget: Long
            get() = budgets.sumOf { it.budget }
    }
}

@Stable
internal sealed interface BudgetEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : BudgetEffect

    @Immutable
    data object NavigateToAdd : BudgetEffect
}

@Stable
internal sealed interface BudgetCostEffect {

    @Immutable
    data object Hidden : BudgetCostEffect

}