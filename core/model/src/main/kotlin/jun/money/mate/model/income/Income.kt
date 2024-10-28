package jun.money.mate.model.income

import jun.money.mate.model.Utils
import java.time.LocalDate

data class Income(
    val id: Long,
    val title: String,
    val amount: Long,
    val type: IncomeType,
    val incomeDate: LocalDate,
    val selected: Boolean = false
) {
    val amountString: String get() = Utils.formatAmountWon(amount)

    val dateString: String get() = when (type) {
        IncomeType.REGULAR -> "매월 ${incomeDate.dayOfMonth}일"
        IncomeType.VARIABLE -> Utils.formatDateToKorean(incomeDate)
    }

    companion object {
        val regularSample = Income(
            id = 1,
            title = "정기 지출 제목",
            amount = 1000000,
            type = IncomeType.REGULAR,
            incomeDate = LocalDate.now(),
            selected = true
        )

        val variableSample = Income(
            id = 2,
            title = "변동 지출 제목",
            amount = 2000000,
            type = IncomeType.VARIABLE,
            incomeDate = LocalDate.now()
        )
    }
}

data class IncomeList(
    val incomes: List<Income>
) {

    val total get() = incomes.sumOf { it.amount }
    val isEmpty get() = total > 0

    val totalString get() = if (isEmpty) Utils.formatAmountWon(total) else "내역이 존재하지 않습니다"

    val regularIncomes get() = incomes.filter { it.type == IncomeType.REGULAR }

    val variableIncomes get() = incomes.filter { it.type == IncomeType.VARIABLE }
}