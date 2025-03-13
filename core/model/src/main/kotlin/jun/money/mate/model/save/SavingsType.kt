package jun.money.mate.model.save

import jun.money.mate.model.serializer.YearMonthSerializer
import jun.money.mate.model.save.SavingsType.PeriodType.Companion.periodEndYearMonth
import kotlinx.serialization.Serializable
import java.time.YearMonth

@Serializable
sealed interface SavingsType {

    interface PaidCount {
        val count: Int
    }

    interface PeriodType {
        @Serializable(with = YearMonthSerializer::class) val periodStart: YearMonth
        val periodMonth: Int

        companion object {
            val PeriodType.periodEndYearMonth: YearMonth
                get() = periodStart.plusMonths(periodMonth.toLong())
        }
    }

    @Serializable
    data class 보통예금(
        override val count: Int  = 0
    ) : SavingsType, PaidCount

    @Serializable
    data class 청약저축(
        override val count: Int  = 0
    ) : SavingsType, PaidCount

    @Serializable
    data class 투자(
        override val count: Int  = 0
    ) : SavingsType, PaidCount

    @Serializable
    data class 적금(
        @Serializable(with = YearMonthSerializer::class) override val periodStart: YearMonth = YearMonth.now(),
        override val periodMonth: Int = 0
    ) : SavingsType, PeriodType

    @Serializable
    data class 보험저축(
        @Serializable(with = YearMonthSerializer::class) override val periodStart: YearMonth = YearMonth.now(),
        override val periodMonth: Int = 0
    ) : SavingsType, PeriodType

    @Serializable
    data class 연금저축(
        override val count: Int  = 0
    ) : SavingsType, PaidCount

    @Serializable
    data class 기타(
        val etc: String = "",
        override val count: Int = 0
    ) : SavingsType, PaidCount

    companion object {

        val SavingsType.title: String
            get() = this::class.simpleName ?: ""

        val SavingsType.periodEnd: YearMonth?
            get() = when (this) {
                is 적금 -> periodEndYearMonth
                is 보험저축 -> periodEndYearMonth
                else -> null
            }

        val allTypes: List<SavingsType> = listOf(
            보통예금(),
            적금(),
            청약저축(),
            투자(),
            보험저축(),
            연금저축(),
            기타()
        )
    }
}