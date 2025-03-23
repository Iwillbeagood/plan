package jun.money.mate.cost.contract

import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.save.ChallengeType
import jun.money.mate.model.spending.CostType
import jun.money.mate.utils.currency.CurrencyFormatter

@Stable
internal data class CostAddState(
    val title: String = "",
    val goalAmount: Long = 0,
    val amount: String = "",
    val count: String = "",
    val dateType: DateType? = null,
    val costType: CostType? = null,
) {

    val goalAmountString get() = if (goalAmount > 0) goalAmount.toString() else ""
    val goalAmountWon get() = if (goalAmount > 0) CurrencyFormatter.formatAmountWon(goalAmount) else ""
}
