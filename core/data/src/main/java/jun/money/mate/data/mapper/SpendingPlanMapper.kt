package jun.money.mate.data.mapper

import jun.money.mate.database.entity.SpendingPlanEntity
import jun.money.mate.model.spending.SpendingPlan

fun SpendingPlanEntity.toSpendingPlan(): SpendingPlan {
    return SpendingPlan(
        id = id,
        title = title,
        type = type,
        spendingCategoryName = spendingCategoryName,
        amount = amount,
        planDay = planDay,
        isApply = isApply,
    )
}