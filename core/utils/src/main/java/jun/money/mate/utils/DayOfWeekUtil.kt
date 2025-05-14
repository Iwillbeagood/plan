package jun.money.mate.utils

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

fun DayOfWeek.toKorean(): String {
    return this.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
}
