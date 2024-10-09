package jun.money.mate.model.spending

import jun.money.mate.model.Utils
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
) {

    val amountString: String get() = "-" + Utils.formatAmountWon(amount)
}

data class SpendingPlanList(
    val spendingPlans: List<SpendingPlan>
) {
    val regularTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.REGULAR_EXPENSE) it.amount else 0.0 }
    val allowanceTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.ALLOWANCE) it.amount else 0.0 }
    val livingTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.LIVING_EXPENSE) it.amount else 0.0 }
    val variableTotal get() = allowanceTotal + livingTotal

    val total get() = regularTotal + variableTotal
    val totalString get() = if (total > 0) "-" + Utils.formatAmountWon(total) else "내역이 존재하지 않습니다"

    val isEmpty get() = spendingPlans.isEmpty()
}