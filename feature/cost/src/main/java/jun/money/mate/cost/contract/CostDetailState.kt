package jun.money.mate.cost.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.spending.CostType
import jun.money.mate.utils.currency.CurrencyFormatter

@Stable
internal sealed interface CostDetailState {

    @Immutable
    data object Loading : CostDetailState

    @Immutable
    data class UiData(
        val id: Long,
        val amount: Long,
        val day: Int,
        val costType: CostType?,
    ) : CostDetailState {

        val amountString get() = if (amount > 0) amount.toString() else ""
        val amountWon get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
    }
}
