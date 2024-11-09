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

    val amountString: String get() = Utils.formatAmountWon(amount)

    val dateString: String get() = "${planDate.dayOfMonth}일"

    val spendingCategory get() = SpendingCategory.find(spendingCategoryName)

    val titleString get() = "${spendingCategory.type.name} | $title"
}

data class SpendingPlanList(
    val spendingPlans: List<SpendingPlan>
) {
    val spendingPlanGroup get() = spendingPlans.groupBy { it.dateString }

    val regularPlans get() = spendingPlans.filter { it.type == SpendingType.PredictedSpending }
    val regularPlansGroup get() = regularPlans.groupBy { it.dateString }

    val consumptionPlan get() = spendingPlans.filter { it.type == SpendingType.ConsumptionPlan }
    val consumptionPlanGroup get() = consumptionPlan.groupBy { it.dateString }

    val regularTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.PredictedSpending) it.amount else 0 }
    val livingTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.ConsumptionPlan) it.amount else 0 }
    val variableTotal get() = livingTotal

    val total get() = regularTotal + variableTotal
    val totalString get() = "-" + Utils.formatAmountWon(total)

    val isEmpty get() = spendingPlans.isEmpty()
}