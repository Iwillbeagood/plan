package jun.money.mate.workmanager.util

import java.time.LocalDate
import java.time.YearMonth

internal fun isLastDayOfMonth(today: LocalDate = LocalDate.now()): Boolean {
    val lastDayOfMonth = YearMonth.now().atEndOfMonth()
    return today == lastDayOfMonth
}
