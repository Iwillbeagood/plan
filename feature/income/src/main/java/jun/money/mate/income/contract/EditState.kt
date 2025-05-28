package jun.money.mate.income.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.DateType
import jun.money.mate.utils.currency.CurrencyFormatter
import java.time.LocalDate

@Stable
internal sealed interface EditState {

    @Immutable
    data object Loading : EditState

    @Immutable
    data class UiData(
        val id: Long,
        val title: String,
        val amount: Long,
        val date: Int,
        val addDate: LocalDate,
        val dateType: DateType,
    ) : EditState {

        val amountString get() = if (amount > 0) amount.toString() else ""
        val amountWon get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
    }
}
