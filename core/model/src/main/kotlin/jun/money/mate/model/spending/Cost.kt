package jun.money.mate.model.spending

import java.time.LocalDate

data class Cost(
    val id: Long,
    val amount: Long,
    val costType: CostType,
    val day: Int,
    val selected: Boolean = false,
) {

    val daysRemaining: Int
        get() {
            val today = LocalDate.now()
            val thisMonth = today.withDayOfMonth(day.coerceAtMost(today.lengthOfMonth()))
            val nextMonth = today.plusMonths(1).withDayOfMonth(day.coerceAtMost(today.plusMonths(1).lengthOfMonth()))

            return if (today.dayOfMonth <= day) {
                // 이번 달에 해당 일이 아직 안 지난 경우
                thisMonth.dayOfMonth - today.dayOfMonth
            } else {
                // 이번 달에 해당 일이 지난 경우 다음 달로 계산
                (nextMonth.toEpochDay() - today.toEpochDay()).toInt()
            }
        }

    val daysRemainingFormatted: String
        get() = when {
            daysRemaining == 0 -> "오늘"
            daysRemaining > 0 -> "${daysRemaining}일 후"
            else -> "지남"
        }

    companion object {
        val samples = listOf(
            Cost(1, 1000, CostType.Normal(NormalType.교통비), 1),
            Cost(2, 2000, CostType.Subscription(SubscriptionType.넷플릭스), 11),
            Cost(1, 1000, CostType.Etc("기타"), 31),
        )
    }
}
