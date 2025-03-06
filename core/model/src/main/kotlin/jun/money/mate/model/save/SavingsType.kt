package jun.money.mate.model.save

import jun.money.mate.model.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed interface SavingsType {

    @Serializable
    data object 보통예금 : SavingsType

    @Serializable
    data class 적금(
        val interest: String = "",
        @Serializable(with = LocalDateSerializer::class) val periodStart: LocalDate = LocalDate.now(),
        @Serializable(with = LocalDateSerializer::class) val periodEnd: LocalDate = LocalDate.now()
    ) : SavingsType

    @Serializable
    data class 청약저축(val interest: String = "") : SavingsType

    @Serializable
    data object 투자 : SavingsType

    @Serializable
    data class 보험저축(
        val interest: String = "",
        @Serializable(with = LocalDateSerializer::class) val periodStart: LocalDate = LocalDate.now(),
        @Serializable(with = LocalDateSerializer::class) val periodEnd: LocalDate = LocalDate.now()
    ) : SavingsType

    @Serializable
    data object 연금저축 : SavingsType

    @Serializable
    data class 기타(val etc: String = "") : SavingsType

    companion object {

        val SavingsType.title: String
            get() = this::class.simpleName ?: ""

        val SavingsType.interest: String?
            get() = when (this) {
                is 적금 -> interest
                is 보험저축 -> interest
                is 청약저축 -> interest
                else -> null
            }

        val SavingsType.periodEnd: LocalDate?
            get() = when (this) {
                is 적금 -> periodEnd
                is 보험저축 -> periodEnd
                else -> null
            }

        val allTypes: List<SavingsType> = listOf(
            보통예금,
            적금(),
            청약저축(),
            투자,
            보험저축(),
            연금저축,
            기타()
        )

        val basicTypes: List<SavingsType> = allTypes.filter { it is 보통예금 || it is 투자 || it is 연금저축 }

        val interestTypes: List<SavingsType> = allTypes.filter { it is 적금 || it is 청약저축 || it is 보험저축 }

        val periodTypes: List<SavingsType> = allTypes.filter { it is 적금 || it is 보험저축 }
    }
}