package jun.money.mate.model.income

import jun.money.mate.model.Utils
import java.time.LocalDate

data class Income(
    val id: Long,
    val title: String,
    val amount: Double,
    val type: IncomeType,
    val incomeDate: LocalDate
) {
    val amountString: String get() = Utils.formatAmountWon(amount)
}

data class IncomeList(
    val incomes: List<Income>
) {

    val total get() = incomes.sumOf { it.amount }
    val isEmpty get() = total > 0

    val totalString get() = if (isEmpty) Utils.formatAmountWon(total) else "내역이 존재하지 않습니다"
}