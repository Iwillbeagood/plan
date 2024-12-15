package jun.money.mate.data.mapper

import jun.money.mate.database.entity.SaveEntity
import jun.money.mate.model.save.SavePlan

internal fun SavePlan.toSaveEntity() = SaveEntity(
    id = id,
    title = title,
    amount = amount,
    planDay = planDay,
    saveCategory = saveCategory,
    executeMonth = executeMonth,
    executed = false,
)

internal fun SaveEntity.toSavePlan() = SavePlan(
    id = id,
    title = title,
    amount = amount,
    planDay = planDay,
    saveCategory = saveCategory,
    executeMonth = executeMonth,
    executed = executed,
    selected = false,
)