package jun.money.mate.save.contract

import androidx.compose.runtime.Stable
import jun.money.mate.model.save.SavingsType
import jun.money.mate.utils.currency.CurrencyFormatter
import java.time.LocalDate

@Stable
internal data class SaveAddState(
    val amount: Long = 0,
    val day: Int = LocalDate.now().dayOfMonth,
    val category: SavingsType? = null,
) {

    val amountString get() = if (amount > 0) amount.toString() else ""
    val amountWon get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
}
