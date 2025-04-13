package jun.money.mate.model.consumption

import jun.money.mate.model.Utils
import java.time.LocalDate
import java.time.YearMonth

data class Budget(
    val id: Long,
    val title: String,
    val budget: Long,
    val pastBudgets: List<PastBudget>,
    val usedList: List<Used>,
) {

    val amountUsed get() = usedList.sumOf { it.amount }

    val budgetLeft get() = budget - amountUsed

    val groupedPastBudget: Map<Int, List<PastBudget>>
        get() = pastBudgets
            .sortedBy { it.date }
            .groupBy { it.date.year }

    private val overBudgetCount get() = pastBudgets.count { it.isOverBudget }

    val usedState get() = when {
        overBudgetCount > 3 -> BudgetUsedState.OverUsed(overBudgetCount = overBudgetCount)
        pastBudgets.size >= 3 && overBudgetCount == 0 -> BudgetUsedState.UnderUsed
        else -> BudgetUsedState.NormalUsed
    }

    val maxUse get() = usedList.maxByOrNull { it.amount }?.amount ?: 0L

    companion object {
        val usedSample = Used(
            id = 1L,
            budgetId = 1L,
            meno = "점심",
            amount = 10000,
            date = LocalDate.now()
        )

        val sample = Budget(
            id = 1L,
            title = "식비",
            budget = 100000,
            pastBudgets = PastBudget.samples,
            usedList = listOf(
                usedSample,
                Used(
                    id = 2L,
                    budgetId = 1L,
                    meno = "저녁",
                    amount = 20000,
                    date = LocalDate.now()
                ),
                Used(
                    id = 3L,
                    budgetId = 3L,
                    meno = "아침",
                    amount = 20000,
                    date = LocalDate.now().plusDays(1)
                ),
            )
        )
    }
}

data class PastBudget(
    val budget: Long,
    val amountUsed: Long,
    val date: YearMonth
) {
    val usageRate get() = (amountUsed.toDouble() / (budget / 0.9)) * 100
    val isOverBudget get() = amountUsed > budget

    fun budgetRate(originBudget: Long): Double {
        return (budget / (originBudget / 0.9)) * 100
    }

    companion object {
        val samples = (1..12).map {
            PastBudget(
                budget = 100000,
                amountUsed = 10000 * (1..12).random().toLong(),
                date = YearMonth.now().minusMonths(it.toLong())
            )
        }
    }
}

data class Used(
    val id: Long = 0,
    val budgetId: Long,
    val meno: String,
    val amount: Long,
    val date: LocalDate,
    val isSelected: Boolean = false,
) {
    val amountString get() = "- " + Utils.formatAmountWon(amount)
}

sealed interface BudgetUsedState {

    data class OverUsed(val overBudgetCount: Int) : BudgetUsedState

    data object UnderUsed : BudgetUsedState

    data object NormalUsed : BudgetUsedState
}