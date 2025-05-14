package jun.money.mate.utils

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

fun canMoveToNextMonth(month: YearMonth): Boolean {
    val currentMonth = YearMonth.now()
    return month.isBefore(currentMonth)
}

fun canMoveToYearBefore(month: YearMonth): Boolean {
    val oneYearAgo = YearMonth.now().minusYears(1)
    return !month.isBefore(oneYearAgo)
}

fun formatDateBasedOnYear(date: LocalDate): String {
    val currentYear = LocalDate.now().year

    return if (date.year == currentYear) {
        date.format(DateTimeFormatter.ofPattern("M월 d일")) // 올해면 "2월"
    } else {
        date.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일")) // 작년 또는 그 외 연도면 "2024년 3월"
    }
}

fun formatDateBasedOnYear(date: YearMonth): String {
    val currentYear = YearMonth.now().year

    return if (date.year == currentYear) {
        date.format(DateTimeFormatter.ofPattern("M월")) // 올해면 "2월"
    } else {
        date.format(DateTimeFormatter.ofPattern("yyyy년 M월")) // 작년 또는 그 외 연도면 "2024년 3월"
    }
}

fun formatYearMonth(date: YearMonth): String {
    val currentYear = YearMonth.now().year

    return date.format(DateTimeFormatter.ofPattern("M월")) // "2월"
}

fun LocalDate.toYearMonth(): YearMonth =
    YearMonth.of(this.year, this.month)

fun Int.toDaysRemaining(): Int {
    val today = LocalDate.now()
    val thisMonth = today.withDayOfMonth(coerceAtMost(today.lengthOfMonth()))
    val nextMonth = today.plusMonths(1).withDayOfMonth(coerceAtMost(today.plusMonths(1).lengthOfMonth()))

    return if (today.dayOfMonth <= this) {
        thisMonth.dayOfMonth - today.dayOfMonth
    } else {
        (nextMonth.toEpochDay() - today.toEpochDay()).toInt()
    }
}

fun Int.toRemainingDayString(): String {
    val daysRemaining = toDaysRemaining()
    return when {
        daysRemaining == 0 -> "오늘"
        daysRemaining > 0 -> "${daysRemaining}일 후"
        else -> "지남"
    }
}
