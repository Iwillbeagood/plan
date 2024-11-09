package jun.money.mate.model.spending

enum class SpendingType(val title: String) {
    ALL("전체"),
    ConsumptionPlan("지출 계획"),
    PredictedSpending("예상 지출");

    companion object {
        val SpendingType.isConsumptionPlan: Boolean
            get() = this == ConsumptionPlan

        val SpendingType.isPredictedSpending: Boolean
            get() = this == PredictedSpending
     }
}