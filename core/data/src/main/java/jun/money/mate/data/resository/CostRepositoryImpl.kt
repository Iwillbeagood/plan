package jun.money.mate.data.resository

import jun.money.mate.data.mapper.toCost
import jun.money.mate.data.mapper.toCostEntity
import jun.money.mate.data_api.database.CostRepository
import jun.money.mate.database.dao.CostDao
import jun.money.mate.database.entity.CostEntity
import jun.money.mate.model.etc.DateType.Companion.isValidForMonth
import jun.money.mate.model.spending.Cost
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject

class CostRepositoryImpl @Inject constructor(
    private val costDao: CostDao
) : CostRepository {

    override suspend fun upsertCost(cost: Cost) {
        try {
            costDao.upsertCost(cost.toCostEntity())
        } catch (e: Exception) {
            Logger.e("upsertCost error: $e")
        }
    }

    override fun getCostFlow(): Flow<List<Cost>> {
        return costDao.getCostFlow()
            .map {
                it.map(CostEntity::toCost)
            }
            .catch {
                Logger.e("getCostFlow error: $it")
            }
    }

    override suspend fun getCostById(id: Long): Cost {
        return costDao.getCostById(id).toCost()
    }

    override fun getCostsByMonth(data: YearMonth): Flow<List<Cost>> {
        return costDao.getCostFlow()
            .map {
                it.filter { cost ->
                    cost.dateType.isValidForMonth(data)
                }.map(CostEntity::toCost)
            }.catch {
                Logger.e("getCostsByMonth error: $it")
            }
    }

    override suspend fun getCostsByMonthList(data: YearMonth): List<Cost> {
        return costDao.getCosts()
            .filter { cost ->
                cost.dateType.isValidForMonth(data)
            }.map(CostEntity::toCost)
    }

    override suspend fun deleteById(id: Long) {
        try {
            costDao.deleteById(id)
        } catch (e: Exception) {
            Logger.e("deleteById error: $e")
        }
    }

    override suspend fun deleteByIds(ids: List<Long>) {
        try {
            costDao.deleteByIds(ids)
        } catch (e: Exception) {
            Logger.e("deleteByIds error: $e")
        }
    }
}