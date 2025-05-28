package jun.money.mate.model.save

import jun.money.mate.model.serializer.DayOfWeekSerializer
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Serializable
sealed interface ChallengeType {

    @Serializable
    data class Weekly(
        @Serializable(with = DayOfWeekSerializer::class) val week: DayOfWeek,
    ) : ChallengeType

    @Serializable
    data class Monthly(val day: Int) : ChallengeType

    companion object {
        fun ChallengeType.dayString(): String {
            return when (val type = this) {
                is Monthly -> "매월 ${type.day}일"
                is Weekly -> "매주 ${type.week.getDisplayName(TextStyle.FULL, Locale.KOREAN)}"
            }
        }

        fun ChallengeType.firstPaymentDate(today: LocalDate = LocalDate.now()): LocalDate {
            return when (this) {
                is Monthly -> {
                    val thisMonthDate = today.withDayOfMonth(day)
                    if (thisMonthDate.isBefore(today)) {
                        thisMonthDate.plusMonths(1)
                    } else {
                        thisMonthDate
                    }
                }
                is Weekly -> {
                    val daysUntilNext = (week.value - today.dayOfWeek.value + 7) % 7
                    if (daysUntilNext == 0) {
                        today
                    } else {
                        today.plusDays(daysUntilNext.toLong())
                    }
                }
            }
        }

        fun ChallengeType.nthPaymentDate(n: Int, today: LocalDate = LocalDate.now()): LocalDate {
            require(n > 0) { "n must be greater than 0" }

            val firstDate = firstPaymentDate(today)
            return when (this) {
                is Monthly -> firstDate.plusMonths((n - 1).toLong())
                is Weekly -> firstDate.plusDays((n - 1) * 7L)
            }
        }
    }
}
