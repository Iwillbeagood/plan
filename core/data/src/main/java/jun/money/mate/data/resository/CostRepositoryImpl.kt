package jun.money.mate.data.resository

import jun.money.mate.data.mapper.toCost
import jun.money.mate.data.mapper.toCostEntity
import jun.money.mate.data_api.database.CostRepository
import jun.money.mate.database.dao.CostDao
import jun.money.mate.database.entity.CostEntity
import jun.money.mate.model.spending.Cost
import jun.money.mate.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
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