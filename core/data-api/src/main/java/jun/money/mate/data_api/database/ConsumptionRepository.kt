package jun.money.mate.data_api.database

import jun.money.mate.model.consumption.Consumption
import jun.money.mate.model.consumption.ConsumptionList
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ConsumptionRepository {

    suspend fun upsertConsumption(consumption: Consumption)

    fun getConsumptionFlow(): Flow<ConsumptionList>

    suspend fun getConsumptionById(
        id: Long
    ): Consumption

    fun getConsumptionByMonth(
        data: LocalDate = LocalDate.now()
    ): Flow<ConsumptionList>

    suspend fun deleteById(id: Long)
}