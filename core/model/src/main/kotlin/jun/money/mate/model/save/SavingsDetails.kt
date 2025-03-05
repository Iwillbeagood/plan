package jun.money.mate.model.save

import java.time.LocalDate

sealed class SavingsDetails(
    val savingsType: SavingsType
) {

    data class Basic(val category: SavingsType) : SavingsDetails(category)

    data class Interest(
        val category: SavingsType,
        val interest: String,
    ) : SavingsDetails(category)

    data class Period(
        val category: SavingsType,
        val interest: String,
        val periodEnd: LocalDate
    ) : SavingsDetails(category)
}