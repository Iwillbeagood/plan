package jun.money.mate.budget.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.consumption.Used

@Stable
internal sealed interface BudgetDetailModalState {

    @Immutable
    data object Hidden : BudgetDetailModalState

    @Immutable
    data class ShowEditBudgetSheet(val recommend: Long = 0) : BudgetDetailModalState

    @Immutable
    data object ShowDeleteDialog : BudgetDetailModalState

    @Immutable
    data object ShowAddUsedSheet : BudgetDetailModalState

    @Immutable
    data class ShowEditUsedSheet(val originUsed: Used) : BudgetDetailModalState
}