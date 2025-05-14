package jun.money.mate.income.contract

import androidx.compose.runtime.Immutable
import jun.money.mate.model.etc.DateType
import jun.money.mate.utils.currency.CurrencyFormatter
import java.time.LocalDate

@Immutable
internal data class IncomeAddState(
    val title: String = "",
    val amount: Long = 0,
    val date: Int = LocalDate.now().dayOfMonth,
    val dateType: DateType? = null,
) {

    val amountString get() = if (amount > 0) amount.toString() else ""
    val amountWon get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
}
