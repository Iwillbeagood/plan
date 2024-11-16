package jun.money.mate.data.mapper

import jun.money.mate.database.entity.ConsumptionEntity
import jun.money.mate.model.consumption.Consumption

fun ConsumptionEntity.toConsumption() = Consumption(
    id = id,
    title = title,
    amount = amount,
    consumptionDate = date,
    planTitle = planTitle,
)