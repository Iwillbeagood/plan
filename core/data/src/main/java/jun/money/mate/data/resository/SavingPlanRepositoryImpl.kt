package jun.money.mate.data.resository

import jun.money.mate.data_api.database.SavingPlanRepository
import jun.money.mate.database.dao.SavingPlanDao
import jun.money.mate.database.entity.SavingPlanEntity
import jun.money.mate.model.saving.SavingPlan
import jun.money.mate.model.saving.SavingPlanList
import jun.money.mate.model.spending.SpendingPlanList
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class SavingPlanRepositoryImpl @Inject constructor(
    private val savingPlanDao: SavingPlanDao
) : SavingPlanRepository {

    override suspend fun upsertSavingPlan(savingPlan: SavingPlan) {
        try {
            savingPlanDao.upsertSavingPlan(
                SavingPlanEntity(
                    title = savingPlan.title,
                    amount = savingPlan.amount,
                    planDate = savingPlan.planDate,
                    executeDate = savingPlan.executeDate,
                    willExecute = savingPlan.willExecute
                )
            )
        } catch (e: Exception) {
            Logger.e("upsertSavingPlan error: $e")
        }
    }

    override fun getSavingPlanFlow(): Flow<SavingPlanList> {
        return savingPlanDao.getSavingPlanFlow().map { list ->
            SavingPlanList(
                list.map {
                    SavingPlan(
                        id = it.id,
                        title = it.title,
                        amount = it.amount,
                        planDate = it.planDate,
                        executeDate = it.executeDate,
                        isExecuted = it.isExecuted,
                        willExecute = it.willExecute
                    )
                }
            )
        }
    }

    override fun getSavingPlansByMonth(year: String, month: String): Flow<SavingPlanList> {
        return savingPlanDao.getSavingPlansByMonth(year, month).map { list ->
            SavingPlanList(
                list.map {
                    SavingPlan(
                        id = it.id,
                        title = it.title,
                        amount = it.amount,
                        planDate = it.planDate,
                        executeDate = it.executeDate,
                        isExecuted = it.isExecuted,
                        willExecute = it.willExecute
                    )
                }
            )
        }
    }

    override suspend fun updateExecuteState(id: Long, executeDate: LocalDate, isExecuted: Boolean) {
        try {
            savingPlanDao.updateExecuteState(id, executeDate, isExecuted)
        } catch (e: Exception) {
            Logger.e("updateExecuteState error: $e")
        }
    }

    override suspend fun updateWillExecuteState(id: Long, willExecute: Boolean) {
        try {
            savingPlanDao.updateWillExecuteState(id, willExecute)
        } catch (e: Exception) {
            Logger.e("updateWillExecuteState error: $e")
        }
    }

    override suspend fun deleteById(id: Long) {
        try {
            savingPlanDao.deleteById(id)
        } catch (e: Exception) {
            Logger.e("deleteById error: $e")
        }
    }
}