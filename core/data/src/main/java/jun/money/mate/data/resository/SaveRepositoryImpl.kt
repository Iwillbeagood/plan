package jun.money.mate.data.resository

import jun.money.mate.data.mapper.toSaveEntity
import jun.money.mate.data.mapper.toSavePlan
import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.database.dao.SaveDao
import jun.money.mate.database.entity.SaveEntity
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.model.save.SavingsType
import jun.money.mate.model.save.SavingsType.PeriodType.Companion.periodEndYearMonth
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject

internal class SaveRepositoryImpl @Inject constructor(
    private val saveDao: SaveDao
) : SaveRepository {

    override suspend fun upsertSavePlan(savePlan: SavePlan) {
        try {
            saveDao.upsert(savePlan.toSaveEntity())
        } catch (e: Exception) {
            Logger.e("upsertSavePlan error: $e")
        }
    }

    override fun getSavePlanListFlow(): Flow<SavePlanList> {
        return saveDao.getFlow().map { list ->
            SavePlanList(savePlans = list.map(SaveEntity::toSavePlan))
        }.catch {
            Logger.e("getSavePlanListFlow error: $it")
        }
    }

    override fun getSavingFlow(date: YearMonth): Flow<SavePlanList> {
        return saveDao.getFlow().map { list ->
            SavePlanList(
                savePlans = list
                    .filterSaveList(date)
                    .map(SaveEntity::toSavePlan)
            )
        }.catch {
            Logger.e("getSavePlanListFlow error: $it")
        }
    }

    override suspend fun getSavePlanListByMonth(date: YearMonth): SavePlanList {
        return SavePlanList(
            savePlans = saveDao.getSavingList()
                .filterSaveList(date)
                .map(SaveEntity::toSavePlan)
        )
    }

    override fun getSavePlan(id: Long): Flow<SavePlan> {
        return saveDao.get(id).map(SaveEntity::toSavePlan)
    }

    override suspend fun updateExecuteState(id: Long, isExecuted: Boolean) {
        try {
            saveDao.updateExecuteState(id, isExecuted)
        } catch (e: Exception) {
            Logger.e("updateExecuteState error: $e")
        }
    }

    override suspend fun deleteById(id: Long) {
        try {
            saveDao.deleteById(id)
        } catch (e: Exception) {
            Logger.e("deleteById error: $e")
        }
    }

    override suspend fun deleteByIds(ids: List<Long>) {
        try {
            saveDao.deleteByIds(ids)
        } catch (e: Exception) {
            Logger.e("deleteByIds error: $e")
        }
    }

    private fun List<SaveEntity>.filterSaveList(date: YearMonth): List<SaveEntity> {
        return this.filter {
            if (it.savingsType is SavingsType.PeriodType) {
                val periodType = it.savingsType as SavingsType.PeriodType
                date in periodType.periodStart..periodType.periodEndYearMonth
            } else {
                it.addYearMonth <= date
            }
        }
    }
}