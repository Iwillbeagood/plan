package jun.money.mate.model.spending

import jun.money.mate.model.Utils
import java.time.LocalDate

data class SpendingPlan(
    val id: Long,
    val title: String,
    val type: SpendingType,
    val spendingCategoryName: String,
    val amount: Long,
    val planDate: LocalDate,
    val isApply: Boolean,
    val selected: Boolean = false
) {

    val amountString: String get() = "-" + Utils.formatAmountWon(amount)

    val dateString: String get() = "${planDate.dayOfMonth}일"

    val spendingCategory get() = SpendingCategory.find(spendingCategoryName)
}

data class SpendingPlanList(
    val spendingPlans: List<SpendingPlan>
) {
    val groupedPlans = spendingPlans.groupBy { it.type }

    val regularTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.REGULAR_EXPENSE) it.amount else 0 }
    val livingTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.LIVING_EXPENSE) it.amount else 0 }
    val variableTotal get() = livingTotal

    val total get() = regularTotal + variableTotal
    val totalString get() = if (total > 0) "-" + Utils.formatAmountWon(total) else "내역이 존재하지 않습니다"

    val isEmpty get() = spendingPlans.isEmpty()
}