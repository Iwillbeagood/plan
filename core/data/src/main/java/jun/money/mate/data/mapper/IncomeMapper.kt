package jun.money.mate.data.mapper

import jun.money.mate.database.entity.IncomeEntity
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList

internal fun Income.toIncomeEntity() = IncomeEntity(
    id = id,
    title = title,
    amount = amount,
    addDate = addDate,
    dateType = dateType,
    date = date
)

internal fun IncomeEntity.toIncome() = Income(
    id = id,
    title = title,
    amount = amount,
    addDate = addDate,
    dateType = dateType,
    date = date,
)

internal fun List<IncomeEntity>.toIncomeList() = IncomeList(
    map(IncomeEntity::toIncome),
)
