package jun.money.mate.model.save

import jun.money.mate.model.Utils

data class SavePlan(
    val id: Long,
    val title: String,
    val amount: Long,
    val planDay: Int,
    val saveCategory: SaveCategory,
    val executeMonth: Int,
    val executed: Boolean,
    val selected: Boolean
) {
    val amountString: String get() = Utils.formatAmountWon(amount)
    val dateString get() = "매월 ${planDay}일"

    val saveState get() = if (executed) SaveState.저금완료 else SaveState.저금예정
}

data class SavePlanList(
    val savePlans: List<SavePlan>
) {

    private val executedTotal get() = savePlans.filter { it.executed }.sumOf { it.amount }
    val executedTotalString get() = Utils.formatAmountWon(executedTotal)

    private val total get() = savePlans.sumOf { it.amount }
    val totalString get() = Utils.formatAmountWon(total)
    val isEmpty get() = savePlans.isEmpty()
}

enum class SaveState {
    저금완료,
    저금예정
}


enum class SaveCategory {
    예금,
    적금,
    청약저축,
    투자,
    보험,
    입출금통장,
    연금저축,
    기타
    ;

    companion object {

    }
}