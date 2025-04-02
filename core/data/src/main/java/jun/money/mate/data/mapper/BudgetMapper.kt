package jun.money.mate.data.mapper

import jun.money.mate.database.entity.BudgetWithUsed
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.consumption.PastBudget
import jun.money.mate.model.consumption.Used

fun BudgetWithUsed.toBudget() = Budget(
    id = budget.id,
    title = budget.title,
    budget = budget.budget,
    amountUsed = budget.amountUsed,
    pastBudgets = if (budget.lastBudget != null && budget.lastAmountUsed != null) {
        PastBudget(
            budget = budget.lastBudget!!,
            amountUsed = budget.lastAmountUsed!!
        )
    } else null,
    usedList = usedList.map {
        Used(
            id = it.id,
            budgetId = it.budgetId,
            title = it.title,
            amount = it.amount,
            date = it.date
        )
    }
)