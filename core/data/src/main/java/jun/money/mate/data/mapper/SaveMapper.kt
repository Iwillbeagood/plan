package jun.money.mate.data.mapper

import jun.money.mate.database.entity.SaveEntity
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.model.save.SavePlan
import java.time.LocalDate

internal fun SavePlan.toSaveEntity() = SaveEntity(
    id = id,
    title = title,
    amount = amount,
    amountGoal = amountGoal,
    planDay = planDay,
    saveType = saveType,
    saveCategory = saveCategory,
    executeMonth = executeMonth,
    executed = false,
    executeCount = executeCount
)

internal fun SaveEntity.toSavePlan() = SavePlan(
    id = id,
    title = title,
    amount = amount,
    amountGoal = amountGoal,
    planDay = planDay,
    saveType = saveType,
    saveCategory = saveCategory,
    executeMonth = executeMonth,
    executed = executed,
    selected = false,
    executeCount = executeCount
)