package jun.money.mate.model.save

import jun.money.mate.model.serializer.DayOfWeekSerializer
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Serializable
sealed interface ChallengeType {

    @Serializable
    data class Weekly(
        @Serializable(with = DayOfWeekSerializer::class) val week: DayOfWeek
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
    }
}