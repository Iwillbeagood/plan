package jun.money.mate.model.save

import jun.money.mate.model.Utils
import jun.money.mate.model.save.SavingsType.Companion.periodEnd
import jun.money.mate.model.save.SavingsType.PeriodType.Companion.periodEndYearMonth
import jun.money.mate.model.save.SavingsType.보험저축
import jun.money.mate.model.save.SavingsType.적금
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

data class SavePlan(
    val id: Long,
    val parentId: Long,
    val amount: Long,
    val day: Int,
    val addYearMonth: YearMonth,
    val savingsType: SavingsType,
    val executed: Boolean,
) {
    val amountString: String get() = Utils.formatAmountWon(amount)

    val period: String
        get() = when (savingsType) {
            is 적금 -> "${savingsType.periodStart} ~ ${savingsType.periodEndYearMonth} (${savingsType.periodMonth}개월)"
            is 보험저축 -> "${savingsType.periodStart} ~ ${savingsType.periodEndYearMonth} (${savingsType.periodMonth}개월)"
            else -> "$addYearMonth ~ "
        }

    fun getPaidCount(): Int {
        if (savingsType is SavingsType.PaidCount) {
            return savingsType.count
        }

        val now = LocalDate.now()
        val todayYearMonth = YearMonth.from(now)

        val startDate = when (savingsType) {
            is 적금 -> LocalDate.of(savingsType.periodStart.year, savingsType.periodStart.month, day)
            is 보험저축 -> LocalDate.of(savingsType.periodStart.year, savingsType.periodStart.month, day)
            else -> return 0
        }

        val monthsBetween = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), todayYearMonth.atDay(1)).toInt()

        return if (now.dayOfMonth < day) 0 else monthsBetween
    }

    val periodTotal: Long
        get() = getPaidCount() * amount

    fun getRemainingPeriod(): String? {
        val today = LocalDate.now()
        val periodEnd = savingsType.periodEnd ?: return null
        val endDate = LocalDate.of(periodEnd.year, periodEnd.month, day).minusDays(1)

        if (today.isAfter(endDate)) return "만기가 지났어요!"
        val months = ChronoUnit.MONTHS.between(today, endDate)
        val daysUntilEnd = ChronoUnit.DAYS.between(today, endDate)

        return when {
            months >= 1 -> "만기까지 ${months}개월 남았어요!"
            else -> "만기까지 ${daysUntilEnd}일만 기다려요!"
        }
    }

    companion object {
        val sample = SavePlan(
            id = 0,
            parentId = 0,
            amount = 10000,
            day = 1,
            addYearMonth = YearMonth.now(),
            savingsType = SavingsType.적금(
                periodStart = YearMonth.now(),
                periodMonth = 6
            ),
            executed = false,
        )
    }
}

data class SavePlanList(
    val savePlans: List<SavePlan>
) {

    val executedTotal get() = savePlans.filter { it.executed }.sumOf { it.amount }
    val total get() = savePlans.filter { it.executed }.sumOf { it.amount }
    val totalString get() = Utils.formatAmountWon(executedTotal)
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