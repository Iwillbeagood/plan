package jun.money.mate.cost.contract

import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.spending.CostType
import jun.money.mate.utils.currency.CurrencyFormatter

@Stable
internal data class CostAddState(
    val amount: Long = 0,
    val day: Int = 0,
    val costType: CostType? = null,
) {

    val amountString get() = if (amount > 0) amount.toString() else ""
    val amountWon get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
}
