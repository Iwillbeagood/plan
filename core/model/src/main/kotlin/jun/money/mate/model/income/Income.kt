package jun.money.mate.model.income

import jun.money.mate.model.etc.DateType
import jun.money.mate.model.Utils
import java.time.LocalDate
import java.time.YearMonth

data class Income(
    val id: Long,
    val title: String,
    val amount: Long,
    val dateType: DateType,
    val isSelected: Boolean = false,
) {
    val amountString: String get() = Utils.formatAmountWon(amount)

    companion object {
        val regularSample = Income(
            id = 1,
            title = "정기 수입 제목",
            amount = 1000000,
            dateType = DateType.Monthly(1, YearMonth.now()),
        )

        val variableSample = Income(
            id = 2,
            title = "변동 수입 제목",
            amount = 2000000,
            dateType = DateType.Specific(LocalDate.now()),

        )
    }
}

data class IncomeList(
    val incomes: List<Income>
) {

    val monthlyTotal get() = incomes.filter { it.dateType is DateType.Monthly }.sumOf { it.amount }
    val specificTotal get() = incomes.filter { it.dateType is DateType.Specific }.sumOf { it.amount }

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