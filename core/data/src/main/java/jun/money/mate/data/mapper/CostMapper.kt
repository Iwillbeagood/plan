package jun.money.mate.data.mapper

import jun.money.mate.database.entity.CostEntity
import jun.money.mate.model.spending.Cost

internal fun Cost.toCostEntity() = CostEntity(
    amount = amount,
    dateType = dateType,
    type = costType
)

internal fun CostEntity.toCost() = Cost(
    id = id,
    amount = amount,
    dateType = dateType,
    costType = type
)