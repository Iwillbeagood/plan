package jun.money.mate.model.save

import jun.money.mate.model.Utils

data class SavePlan(
    val id: Long,
    val title: String,
    val amount: Long,
    val planDay: Int,
    val executeMonth: Int,
    val executed: Boolean,
) {
    val amountString: String get() = Utils.formatAmountWon(amount)
}

data class SavePlanList(
    val savePlans: List<SavePlan>
) {

    val total get() = savePlans.sumOf { it.amount }
    val totalString get() = Utils.formatAmountWon(total)
    val isEmpty get() = savePlans.isEmpty()
}