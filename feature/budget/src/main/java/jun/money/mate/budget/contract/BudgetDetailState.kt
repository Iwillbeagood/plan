package jun.money.mate.budget.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.consumption.Used

@Stable
internal sealed interface BudgetDetailState {

    @Immutable
    data object Loading : BudgetDetailState

    @Immutable
    data class BudgetDetailData(
        val budget: Budget,
    ) : BudgetDetailState {

        val selectedUsed get() = budget.usedList.filter(Used::isSelected).map(Used::id)
        val selectedCount get() = selectedUsed.count()
    }
}
