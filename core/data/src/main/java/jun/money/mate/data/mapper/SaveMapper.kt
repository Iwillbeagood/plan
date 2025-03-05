package jun.money.mate.data.mapper

import jun.money.mate.database.entity.SaveEntity
import jun.money.mate.model.save.SavePlan

internal fun SavePlan.toSaveEntity() = SaveEntity(
    id = id,
    amount = amount,
    planDay = planDay,
    savingsType = savingsType,
    executeMonth = executeMonth,
    executed = false,
)

internal fun SaveEntity.toSavePlan() = SavePlan(
    id = id,
    amount = amount,
    planDay = planDay,
    savingsType = savingsType,
    executeMonth = executeMonth,
    executed = executed,
    selected = false,
)