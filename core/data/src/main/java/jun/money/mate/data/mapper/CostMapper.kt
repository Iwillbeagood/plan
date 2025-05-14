package jun.money.mate.data.mapper

import jun.money.mate.database.entity.CostEntity
import jun.money.mate.model.spending.Cost

internal fun Cost.toCostEntity() = CostEntity(
    id = id,
    amount = amount,
    day = day,
    type = costType,
)

internal fun CostEntity.toCost() = Cost(
    id = id,
    amount = amount,
    day = day,
    costType = type,
)
