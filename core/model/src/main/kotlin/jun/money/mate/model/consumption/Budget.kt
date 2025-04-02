package jun.money.mate.model.consumption

import jun.money.mate.model.Utils
import java.time.LocalDate
import java.time.YearMonth

data class Budget(
    val id: Long,
    val title: String,
    val budget: Long,
    val amountUsed: Long,
    val pastBudgets: List<PastBudget>,
    val usedList: List<Used>,
) {

    val amountString get() = "- " + Utils.formatAmountWon(amountUsed)

    companion object {
        val sample = Budget(
            id = 1L,
            title = "식비",
            budget = 100000,
            amountUsed = 50000,
            pastBudgets = emptyList(),
            usedList = listOf(
                Used(
                    id = 1L,
                    budgetId = 1L,
                    title = "점심",
                    amount = 10000,
                    date = LocalDate.now()
                ),
                Used(
                    id = 2L,
                    budgetId = 1L,
                    title = "저녁",
                    amount = 20000,
                    date = LocalDate.now()
                )
            )
        )
    }
}

data class PastBudget(
    val budget: Long,
    val amountUsed: Long,
    val date: YearMonth
)

data class Used(
    val id: Long,
    val budgetId: Long,
    val title: String,
    val amount: Long,
    val date: LocalDate,
) {
    val amountString get() = "- " + Utils.formatAmountWon(amount)
}