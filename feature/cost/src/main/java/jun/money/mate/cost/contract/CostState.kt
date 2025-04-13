package jun.money.mate.cost.contract

import jun.money.mate.cost.component.CostCalendarValue
import jun.money.mate.model.Utils
import jun.money.mate.model.etc.DateType.Companion.date
import jun.money.mate.model.spending.Cost

internal sealed interface CostState {

    data object Loading : CostState

    data class Data(
        val costs: List<Cost>,
        val selectedCalendarValue: CostCalendarValue? = null,
    ) : CostState {

        val costsByCalendar get() = costs.filter {
            if (selectedCalendarValue == null) {
                true
            } else {
                it.day == selectedCalendarValue.date
            }
        }

        val totalCostString get() = Utils.formatAmountWon(costs.sumOf { it.amount })

        val costCalendarValues get() = costs.groupBy { it.day }.map {
            CostCalendarValue(it.key, it.value)
        }

        val selectedCosts get() = costs.filter(Cost::selected).map(Cost::id)
        val selectedCount get() = selectedCosts.count()
    }
}