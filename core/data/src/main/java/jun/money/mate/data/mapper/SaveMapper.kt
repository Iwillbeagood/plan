package jun.money.mate.data.mapper

import jun.money.mate.database.entity.SaveEntity
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.model.save.SavePlan
import java.time.LocalDate

fun SavePlan.toSaveEntity() = SaveEntity(
    id = id,
    title = title,
    amount = amount,
    planDay = planDay,
    saveCategory = saveCategory,
    executeMonth = executeMonth,
    executed = false
)

fun SaveEntity.toSavePlan() = SavePlan(
    id = id,
    title = title,
    amount = amount,
    planDay = planDay,
    saveCategory = saveCategory,
    executeMonth = executeMonth,
    executed = if (executeMonth == LocalDate.now().monthValue) {
        executed
    } else {
        false
    },
    selected = false
)

fun List<SaveEntity>.toSaveList() = SavePlanList(
    savePlans = map {
        it.toSavePlan()
    }
)