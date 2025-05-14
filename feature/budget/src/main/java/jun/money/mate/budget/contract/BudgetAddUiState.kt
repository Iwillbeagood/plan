package jun.money.mate.budget.contract

import androidx.compose.runtime.Immutable
import jun.money.mate.utils.currency.CurrencyFormatter

@Immutable
internal data class BudgetAddUiState(
    val title: String = "",
    val budget: Long = 0,
) {

    val amountString get() = if (budget > 0) budget.toString() else ""
    val amountWon get() = if (budget > 0) CurrencyFormatter.formatAmountWon(budget) else ""
}
