package jun.money.mate.data.mapper

import jun.money.mate.database.entity.SaveEntity
import jun.money.mate.model.save.SavePlan

internal fun SavePlan.toSaveEntity() = SaveEntity(
    id = id,
    amount = amount,
    day = day,
    savingsType = savingsType,
    executed = executed,
)

internal fun SaveEntity.toSavePlan() = SavePlan(
    id = id,
    amount = amount,
    day = day,
    savingsType = savingsType,
    executed = executed,
)