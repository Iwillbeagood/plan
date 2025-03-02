package jun.money.mate.designsystem_date.datetimepicker

import java.time.LocalDate

object TimeBoundaries {
    val lastMonthToThisMonth: ClosedRange<LocalDate> = LocalDate.now().let { now ->
        val startOfThisMonth = now.withDayOfMonth(1)
        val endOfThisMonth = now.withDayOfMonth(now.lengthOfMonth())

        val startOfLastMonth = startOfThisMonth.minusMonths(1)
        val endOfLastMonth = endOfThisMonth.minusMonths(1)

        startOfLastMonth..endOfThisMonth
    }
}