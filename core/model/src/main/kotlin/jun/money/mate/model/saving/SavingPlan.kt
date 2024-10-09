package jun.money.mate.model.saving

import jun.money.mate.model.Utils
import java.time.LocalDate

data class SavingPlan(
    val id: Long,
    val title: String,
    val amount: Double,
    val planDate: LocalDate,
    val executeDate: LocalDate,
    val isExecuted: Boolean,
    val willExecute: Boolean
) {
    val amountString: String get() = Utils.formatAmountWon(amount)
}

data class SavingPlanList(
    val savingPlans: List<SavingPlan>
) {
    val total get() = savingPlans.sumOf { it.amount }
    val totalString get() = Utils.formatAmountWon(total)
    val isEmpty get() = savingPlans.isEmpty()
}