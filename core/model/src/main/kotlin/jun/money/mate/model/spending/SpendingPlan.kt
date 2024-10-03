package jun.money.mate.model.spending

import java.time.LocalDate

data class SpendingPlan(
    val id: Long,
    val title: String,
    val type: SpendingType,
    val amount: Double,
    val planDate: LocalDate,
    val executeDate: LocalDate,
    val isExecuted: Boolean,
    val notificationEnabled: Boolean,
    val willExecute: Boolean
)