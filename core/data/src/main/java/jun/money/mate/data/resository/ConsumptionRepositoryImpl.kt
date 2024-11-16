package jun.money.mate.data.resository

import jun.money.mate.data.mapper.toConsumption
import jun.money.mate.data_api.database.ConsumptionRepository
import jun.money.mate.database.dao.ConsumptionDao
import jun.money.mate.database.entity.ConsumptionEntity
import jun.money.mate.model.consumption.Consumption
import jun.money.mate.model.consumption.ConsumptionList
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ConsumptionRepositoryImpl @Inject constructor(
     private val consumptionDao: ConsumptionDao
) : ConsumptionRepository {

    override suspend fun upsertConsumption(consumption: Consumption) {
        try {
            consumptionDao.upsertConsumption(
                ConsumptionEntity(
                    id = consumption.id,
                    title = consumption.title,
                    amount = consumption.amount,
                    date = consumption.consumptionDate,
                    planTitle = consumption.planTitle,
                )
            )
        } catch (e: Exception) {
            Logger.e("upsertConsumption error: $e")
        }
    }

    override fun getConsumptionFlow(): Flow<ConsumptionList> {
        return consumptionDao.getConsumptionFlow().map { list ->
            ConsumptionList(
                list.map {
                   it.toConsumption()
                }
            )
        }.catch {
            Logger.e("getConsumptionFlow error: $it")
        }
    }

    override suspend fun getConsumptionById(id: Long): Consumption {
        return consumptionDao.getConsumptionById(id).toConsumption()
    }

    override fun getConsumptionByMonth(data: LocalDate): Flow<ConsumptionList> {
        return consumptionDao.getConsumptionByMonth(
            year = data.year.toString(),
            month = data.monthValue.toString()
        ).map { list ->
            ConsumptionList(
                list.map { it.toConsumption() }
            )
        }.catch {
            Logger.e("getConsumptionByMonth error: $it")
        }
    }

    override suspend fun deleteById(id: Long) {
        try {
            consumptionDao.deleteConsumptionById(id)
        } catch (e: Exception) {
            Logger.e("deleteById error: $e")
        }
    }
}