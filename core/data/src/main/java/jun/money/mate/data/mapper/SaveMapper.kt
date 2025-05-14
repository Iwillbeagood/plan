package jun.money.mate.data.mapper

import jun.money.mate.database.entity.SaveEntity
import jun.money.mate.model.save.SavePlan

internal fun SavePlan.toSaveEntity() = SaveEntity(
    id = id,
    parentId = parentId,
    amount = amount,
    day = day,
    addYearMonth = addYearMonth,
    savingsType = savingsType,
    executed = executed,
)

internal fun SaveEntity.toSavePlan() = SavePlan(
    id = id,
    parentId = parentId,
    amount = amount,
    day = day,
    addYearMonth = addYearMonth,
    savingsType = savingsType,
    executed = executed,
)
