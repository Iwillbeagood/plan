package jun.money.mate.model.etc

import jun.money.mate.model.LocalDateSerializer
import jun.money.mate.model.Utils.formatDateToKorean
import jun.money.mate.model.YearMonthSerializer
import jun.money.mate.model.etc.DateType.Monthly.Companion.isValidForMonthly
import jun.money.mate.model.etc.DateType.Specific.Companion.isValidForSpecific
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.YearMonth

@Serializable
sealed interface DateType {

    /**
     *  정기 수입을 add 할 때, 날짜를 기록하고 해당 날짜 이후로만 탐색 가능하게 함.
     *  삭제, 수정시에 expireDate를 추가함
     *  나중에 Monthly 데이터를 보여줄 떄, 해당 달이 addDate 이상이고 expiredDate 초과여야함.
     * */
    @Serializable
    data class Monthly(
        val day: Int,
        @Serializable(with = YearMonthSerializer::class) val addDate: YearMonth,
        @Serializable(with = LocalDateSerializer::class) val expiredDate: LocalDate? = null
    ) : DateType {

        companion object {
            fun Monthly.isValidForMonthly(target: YearMonth): Boolean {
                return target >= addDate && (expiredDate == null || target <= YearMonth.from(expiredDate))
            }
        }
    }

    @Serializable
    data class Specific(
        @Serializable(with = LocalDateSerializer::class) val date: LocalDate
    ) : DateType {

        companion object {
            fun Specific.isValidForSpecific(target: YearMonth): Boolean {
                return target == YearMonth.from(date)
            }
        }
    }

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

        fun DateType.isValidForMonth(target: YearMonth): Boolean {
            return when (this) {
                is Monthly -> isValidForMonthly(target)
                is Specific -> isValidForSpecific(target)
            }
        }
    }
}