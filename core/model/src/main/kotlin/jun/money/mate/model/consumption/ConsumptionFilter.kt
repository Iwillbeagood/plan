package jun.money.mate.model.consumption

import jun.money.mate.model.spending.SpendingPlanList

data class ConsumptionFilter(
    val planTitle: String,
    val selected: Boolean
) {

    companion object {

        const val ALL_TITLE = "전체"

        val ALL = ConsumptionFilter(ALL_TITLE, true)

        fun SpendingPlanList.toConsumptionFilter() =
            listOf(ALL) + consumptionPlan.map {
                ConsumptionFilter(
                    it.title,
                    false
                )
            }

        fun List<ConsumptionFilter>.selectedFilter() = find { it.selected } ?: ALL

        fun List<ConsumptionFilter>.toStringList() = map { it.planTitle }
    }
}
