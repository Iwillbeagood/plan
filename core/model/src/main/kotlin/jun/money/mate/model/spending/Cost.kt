package jun.money.mate.model.spending

import jun.money.mate.model.etc.DateType
import java.time.LocalDate
import java.time.YearMonth

data class Cost(
    val id: Long,
    val amount: Long,
    val costType: CostType,
    val dateType: DateType,
) {
    companion object {
        val samples = listOf(
            Cost(1, 1000, CostType.Normal(NormalType.교통비), DateType.Monthly(1, YearMonth.now())),
            Cost(2, 2000, CostType.Subscription(SubscriptionType.넷플릭스), DateType.Specific(LocalDate.now())),
            Cost(1, 1000, CostType.Etc("기타"), DateType.Monthly(1, YearMonth.now())),
        )
    }
}
