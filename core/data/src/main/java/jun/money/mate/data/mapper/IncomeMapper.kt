package jun.money.mate.data.mapper

import jun.money.mate.database.entity.IncomeEntity
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList

internal fun Income.toIncomeEntity() = IncomeEntity(
    id = id,
    title = title,
    amount = amount,
    dateType = dateType,
)

internal fun IncomeEntity.toIncome() = Income(
    id = id,
    title = title,
    amount = amount,
    dateType = dateType,
)

internal fun List<IncomeEntity>.toIncomeList() = IncomeList(
    map(IncomeEntity::toIncome)
)