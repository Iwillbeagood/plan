package jun.money.mate.model.save

import jun.money.mate.model.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed interface SavingsType {

    @Serializable
    data object 보통예금 : SavingsType

    @Serializable
    data class 정기예금(
        val interest: String,
        @Serializable(with = LocalDateSerializer::class) val periodEnd: LocalDate
    ) : SavingsType

    @Serializable
    data class 적금(
        val interest: String,
        @Serializable(with = LocalDateSerializer::class) val periodEnd: LocalDate
    ) : SavingsType

    @Serializable
    data class 청약저축(val interest: String) : SavingsType

    @Serializable
    data object 투자 : SavingsType

    @Serializable
    data class 보험저축(
        val interest: String,
        @Serializable(with = LocalDateSerializer::class) val periodEnd: LocalDate
    ) : SavingsType

    @Serializable
    data object 연금저축 : SavingsType

    @Serializable
    data class 기타(val etc: String) : SavingsType

    companion object {
        val allTypes: List<SavingsType> = listOf(
            보통예금,
            정기예금("", LocalDate.now()),
            적금("", LocalDate.now()),
            청약저축(""),
            투자,
            보험저축("", LocalDate.now()),
            연금저축,
            기타("")
        )

        val basicTypes: List<SavingsType> = listOf(
            보통예금,
            투자,
            연금저축
        )

        val interestTypes: List<SavingsType> = listOf(
            정기예금("", LocalDate.now()),
            적금("", LocalDate.now()),
            청약저축(""),
            보험저축("", LocalDate.now())
        )

        val periodTypes: List<SavingsType> = listOf(
            정기예금("", LocalDate.now()),
            적금("", LocalDate.now()),
            보험저축("", LocalDate.now())
        )
    }
}