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
    val total get() = spendingPlans.sumOf { it.amount }
    val isEmpty get() = spendingPlans.isEmpty()
}