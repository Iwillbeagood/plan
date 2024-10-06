package jun.money.mate.model.saving

import java.time.LocalDate

data class SavingPlan(
    val id: Long,
    val title: String,
    val amount: Double,
    val planDate: LocalDate,
    val executeDate: LocalDate,
    val isExecuted: Boolean,
    val willExecute: Boolean
)

data class SavingPlanList(
    val savingPlans: List<SavingPlan>
) {
    val total get() = savingPlans.sumOf { it.amount }
    val isEmpty get() = savingPlans.isEmpty()
}