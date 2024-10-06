package jun.money.mate.model.spending

import java.time.LocalDate

data class SpendingPlan(
    val id: Long,
    val title: String,
    val type: SpendingType,
    val amount: Double,
    val planDate: LocalDate,
    val executeDate: LocalDate,
    val isExecuted: Boolean,
    val willExecute: Boolean
)

data class SpendingPlanList(
    val spendingPlans: List<SpendingPlan>
) {
    val regularTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.REGULAR_EXPENSE) it.amount else 0.0 }
    val allowanceTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.ALLOWANCE) it.amount else 0.0 }
    val livingTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.LIVING_EXPENSE) it.amount else 0.0 }
    val variableTotal get() = allowanceTotal + livingTotal
    val total get() = regularTotal + variableTotal

    val isEmpty get() = spendingPlans.isEmpty()
}