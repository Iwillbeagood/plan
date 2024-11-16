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

    val predictPlans get() = spendingPlans.filter { it.type == SpendingType.PredictedSpending }
    val predictPlanGroup get() = predictPlans
        .groupBy { it.dateString }
        .toSortedMap()

    val consumptionPlan get() = spendingPlans.filter { it.type == SpendingType.ConsumptionPlan }

    val predictTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.PredictedSpending) it.amount else 0 }
    val consumptionTotal get() = spendingPlans.sumOf { if (it.type == SpendingType.ConsumptionPlan) it.amount else 0 }

    val total get() = predictTotal + consumptionTotal
    val totalString get() = Utils.formatAmountWon(total)

    val isEmpty get() = spendingPlans.isEmpty()
}


data class ConsumptionSpend(
    val spendingPlan: SpendingPlan,
    val consumptionTotal: Long
) {

    val totalString get() = "- " + Utils.formatAmountWon(consumptionTotal)

    private val remaining get() = spendingPlan.amount - consumptionTotal
    val remainingString get() = Utils.formatAmountWon(remaining)
}