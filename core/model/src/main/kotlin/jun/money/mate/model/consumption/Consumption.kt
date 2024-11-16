package jun.money.mate.model.consumption

import jun.money.mate.model.Utils
import java.time.LocalDate

data class Consumption(
    val id: Long,
    val title: String,
    val amount: Long,
    val consumptionDate: LocalDate,
    val planTitle: String,
    val selected: Boolean = false
) {

    val amountString get() = "- " + Utils.formatAmountWon(amount)

    val dateString get() = "${consumptionDate.dayOfMonth}일"

}

data class ConsumptionList(
    val consumptions: List<Consumption>
) {

    val consumptionsGroup: List<ConsumptionGroup> get() = consumptions.groupBy { it.dateString }.map {
        ConsumptionGroup(it.key, it.value)
    }

    val isEmpty get() = consumptions.isEmpty()

    val total get() = consumptions.sumOf { it.amount }
    val totalString get() = "- " + Utils.formatAmountWon(total)
}

data class ConsumptionGroup(
    val date: String,
    val consumptions: List<Consumption>
) {
    private val total get() = consumptions.sumOf { it.amount }
    val totalString get() = "- " + Utils.formatAmountWon(total)
}