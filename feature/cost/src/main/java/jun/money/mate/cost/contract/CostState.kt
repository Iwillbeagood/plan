package jun.money.mate.cost.contract

import jun.money.mate.cost.component.CostCalendarValue
import jun.money.mate.model.Utils
import jun.money.mate.model.etc.DateType.Companion.date
import jun.money.mate.model.spending.Cost

internal sealed interface CostState {

    data object Loading : CostState

    data class Data(
        val costs: List<Cost>
    ) : CostState {

        val totalCostString get() = Utils.formatAmountWon(costs.sumOf { it.amount })

        val costCalendarValues get() = costs.groupBy { it.dateType.date.dayOfMonth }.map {
            CostCalendarValue(it.key, it.value)
        }
    }
}