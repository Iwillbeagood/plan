package jun.money.mate.model.etc

import jun.money.mate.model.LocalDateSerializer
import jun.money.mate.model.Utils.formatDateToKorean
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed interface DateType {

    @Serializable
    data class Monthly(val day: Int) : DateType

    @Serializable
    data class Specific(
        @Serializable(with = LocalDateSerializer::class) val date: LocalDate
    ) : DateType

    companion object {

        val DateType.title: String
            get() {
                return when (this) {
                    is Monthly -> "정기"
                    is Specific -> "단기"
                }
            }

        val DateType.date: LocalDate
            get() {
                return when (this) {
                    is Monthly -> LocalDate.of(LocalDate.now().year, LocalDate.now().month, day)
                    is Specific -> date
                }
            }

        fun DateType.toDateString(): String {
            return when (this) {
                is Monthly -> "매월 ${day}일"
                is Specific -> formatDateToKorean(date)
            }
        }
    }
}