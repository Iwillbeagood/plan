package jun.money.mate.model.spending

data class Cost(
    val id: Long,
    val amount: Long,
    val costType: CostType,
    val day: Int,
    val selected: Boolean = false,
) {
    companion object {
        val samples = listOf(
            Cost(1, 1000, CostType.Normal(NormalType.교통비), 1),
            Cost(2, 2000, CostType.Subscription(SubscriptionType.넷플릭스), 11),
            Cost(1, 1000, CostType.Etc("기타"), 31),
        )
    }
}
