package jun.money.mate.model.income

import java.time.LocalDate

data class Income(
    val id: Long,
    val title: String,
    val amount: Double,
    val type: IncomeType,
    val incomeDate: LocalDate,
    val isExecuted: Boolean,
)