package jun.money.mate.model.save

import jun.money.mate.model.Utils
import jun.money.mate.model.save.SavingsType.Companion.periodEnd
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class SavePlan(
    val id: Long,
    val amount: Long,
    val day: Int,
    val savingsType: SavingsType,
    val executed: Boolean,
    val selected: Boolean = false,
) {
    val amountString: String get() = Utils.formatAmountWon(amount)

    fun getRemainingPeriod(): String? {
        val today = LocalDate.now()
        val periodEnd = savingsType.periodEnd ?: return null
        val endDate = LocalDate.of(periodEnd.year, periodEnd.month, day).minusDays(1)

        if (today.isAfter(endDate)) return "만기가 지났어요!"
        val months = ChronoUnit.MONTHS.between(today, endDate)
        val daysUntilEnd = ChronoUnit.DAYS.between(today, endDate)

        return when {
            months >= 1 -> "${months}개월 남았어요!"
            else -> "${daysUntilEnd}일만 기다려요!"
        }
    }

    val saveState get() = if (executed) SaveState.저축완료 else SaveState.저축예정

    companion object {
        val sample = SavePlan(
            id = 0,
            amount = 10000,
            day = 1,
            savingsType = SavingsType.투자,
            executed = false,
            selected = false
        )
    }
}

data class SavePlanList(
    val savePlans: List<SavePlan>
) {

    val total get() = savePlans.filter { it.executed }.sumOf { it.amount }
    val totalString get() = Utils.formatAmountWon(total)
    val isEmpty get() = savePlans.isEmpty()

    companion object {
        val sample = SavePlanList(
            savePlans = listOf(
                SavePlan.sample,
                SavePlan.sample
            )
        )
    }
}

enum class SaveState {
    저축완료,
    저축예정
}