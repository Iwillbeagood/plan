package jun.money.mate.model.save

import jun.money.mate.model.Utils

data class SavePlan(
    val id: Long,
    val title: String,
    val amount: Long,
    val planDay: Int,
    val saveType: SaveType,
    val saveCategory: SaveCategory,
    val executeMonth: Int,
    val executed: Boolean,
    val selected: Boolean,
    val amountGoal: Long = 0,
    val executeCount: Int = 0,
) {
    val amountString: String get() = Utils.formatAmountWon(amount)
    val amountGoalString: String get() = Utils.formatAmountWon(amountGoal)

    val goalMonth get() = amountGoal / amount

    val dateString get() = "매월 ${planDay}일"

    val saveState get() = if (executed) SaveState.저금완료 else SaveState.저금예정

    companion object {
        val sample = SavePlan(
            id = 0,
            title = "title",
            amount = 10000,
            planDay = 1,
            executeMonth = 1,
            saveCategory = SaveCategory.투자,
            saveType = SaveType.ContinueSave,
            executed = false,
            selected = false
        )

        val sample2 = SavePlan(
            id = 0,
            title = "title",
            amount = 10000,
            planDay = 1,
            executeMonth = 1,
            saveCategory = SaveCategory.투자,
            saveType = SaveType.PlaningSave,
            executed = false,
            selected = false
        )
    }
}

data class SavePlanList(
    val savePlans: List<SavePlan>
) {
    val continuePlans get() = savePlans.filter { it.saveType == SaveType.ContinueSave }
    val planingPlans get() = savePlans.filter { it.saveType == SaveType.PlaningSave }

    private val executedTotal get() = savePlans.filter { it.executed }.sumOf { it.amount }
    val executedTotalString get() = Utils.formatAmountWon(executedTotal)

    private val total get() = savePlans.sumOf { it.amount }
    val totalString get() = Utils.formatAmountWon(total)
    val isEmpty get() = savePlans.isEmpty()

    companion object {
        val sample = SavePlanList(
            savePlans = listOf(
                SavePlan(
                    id = 1,
                    title = "주식",
                    amount = 500000,
                    planDay = 10,
                    saveType = SaveType.ContinueSave,
                    saveCategory = SaveCategory.투자,
                    executeMonth = 1,
                    executed = false,
                    selected = false
                ),
                SavePlan(
                    id = 2,
                    title = "예금",
                    amount = 10000,
                    planDay = 10,
                    saveType = SaveType.PlaningSave,
                    saveCategory = SaveCategory.연금저축,
                    executeMonth = 1,
                    executed = true,
                    selected = false
                )
            )
        )
    }
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