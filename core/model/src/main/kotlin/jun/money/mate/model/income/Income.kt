package jun.money.mate.model.income

import jun.money.mate.model.Utils
import jun.money.mate.model.etc.DateType
import java.time.LocalDate
import java.time.YearMonth

data class Income(
    val id: Long,
    val title: String,
    val amount: Long,
    val date: Int,
    val addDate: LocalDate,
    val dateType: DateType,
    val isSelected: Boolean = false,
) {
    val localDate get() = LocalDate.of(addDate.year, addDate.month, date)

    val addYearMonth get() = YearMonth.from(addDate)
    val amountString: String get() = Utils.formatAmountWon(amount)

    companion object {
        val regularSample = Income(
            id = 1,
            title = "정기 수입 제목",
            amount = 1000000,
            date = 1,
            addDate = LocalDate.now(),
            dateType = DateType.Monthly,
        )

        val variableSample = Income(
            id = 2,
            title = "변동 수입 제목",
            amount = 2000000,
            date = 1,
            addDate = LocalDate.now(),
            dateType = DateType.Specific,
        )
    }
}

data class IncomeList(
    val incomes: List<Income>
) {

    val monthlyTotal get() = incomes.filter { it.dateType == DateType.Monthly }.sumOf { it.amount }
    val specificTotal get() = incomes.filter { it.dateType == DateType.Specific }.sumOf { it.amount }

    val total get() = incomes.sumOf { it.amount }
    val isEmpty get() = total > 0

    val totalString get() = Utils.formatAmountWon(total)

    companion object {
        val sample = IncomeList(
            incomes = listOf(
                Income.regularSample,
                Income.variableSample,
            )
        )
    }
}