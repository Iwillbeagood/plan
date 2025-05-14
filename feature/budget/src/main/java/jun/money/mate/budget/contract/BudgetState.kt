package jun.money.mate.budget.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.consumption.Budget

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
