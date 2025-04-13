package jun.money.mate.data.mapper

import jun.money.mate.database.entity.BudgetWithUsed
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.consumption.PastBudget
import jun.money.mate.model.consumption.Used

fun BudgetWithUsed.toBudget() = Budget(
    id = budget.id,
    title = budget.title,
    budget = budget.budget,
    pastBudgets = pastBudgets.map {
        PastBudget(
            budget = it.budget,
            amountUsed = it.amountUsed,
            date = it.date
        )
    },
    usedList = usedList.map {
        Used(
            id = it.id,
            budgetId = it.budgetId,
            meno = it.title,
            amount = it.amount,
            date = it.date
        )
    }
)