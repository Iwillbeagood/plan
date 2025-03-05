package jun.money.mate.model.save

import jun.money.mate.model.Utils

data class SavePlan(
    val id: Long,
    val amount: Long,
    val planDay: Int,
    val savingsType: SavingsType,
    val executeMonth: Int,
    val executed: Boolean,
    val selected: Boolean,
) {
    val amountString: String get() = Utils.formatAmountWon(amount)

    val dateString get() = "매월 ${planDay}일"

    val saveState get() = if (executed) SaveState.저축완료 else SaveState.저축예정

    companion object {
        val sample = SavePlan(
            id = 0,
            amount = 10000,
            planDay = 1,
            executeMonth = 1,
            savingsType = SavingsType.투자,
            executed = false,
            selected = false
        )

        val sample2 = SavePlan(
            id = 0,
            amount = 10000,
            planDay = 1,
            executeMonth = 1,
            savingsType = SavingsType.투자,
            executed = false,
            selected = false
        )
    }
}

data class SavePlanList(
    val savePlans: List<SavePlan>
) {

    val executedTotal get() = savePlans.filter { it.executed }.sumOf { it.amount }
    val executedTotalString get() = Utils.formatAmountWon(executedTotal)

    val total get() = savePlans.sumOf { it.amount }
    val totalString get() = Utils.formatAmountWon(total)
    val isEmpty get() = savePlans.isEmpty()

    companion object {
        val sample = SavePlanList(
            savePlans = listOf(
                SavePlan(
                    id = 1,
                    amount = 500000,
                    planDay = 10,
                    savingsType = SavingsType.투자,
                    executeMonth = 1,
                    executed = false,
                    selected = false
                ),
                SavePlan(
                    id = 2,
                    amount = 10000,
                    planDay = 10,
                    savingsType = SavingsType.연금저축,
                    executeMonth = 1,
                    executed = true,
                    selected = false
                )
            )
        )
    }
}

enum class SaveState {
    저축완료,
    저축예정
}