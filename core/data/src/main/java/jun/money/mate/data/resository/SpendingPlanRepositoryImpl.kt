package jun.money.mate.data.resository

import jun.money.mate.data_api.database.SpendingPlanRepository
import jun.money.mate.database.dao.SpendingPlanDao
import jun.money.mate.database.entity.SpendingPlanEntity
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingPlanList
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class SpendingPlanRepositoryImpl @Inject constructor(
    private val spendingPlanDao: SpendingPlanDao
) : SpendingPlanRepository {

    override suspend fun upsertSpendingPlan(spendingPlan: SpendingPlan) {
        try {
            spendingPlanDao.upsertSpendingPlan(
                SpendingPlanEntity(
                    title = spendingPlan.title,
                    type = spendingPlan.type,
                    amount = spendingPlan.amount,
                    planDate = spendingPlan.planDate,
                    executeDate = spendingPlan.executeDate,
                    isExecuted = spendingPlan.isExecuted,
                    willExecute = spendingPlan.willExecute
                )
            )
        } catch (e: Exception) {
            Logger.e("upsertSpendingPlan error: $e")
        }
    }

    override fun getSpendingPlanFlow(): Flow<SpendingPlanList> {
        return spendingPlanDao.getSpendingPlanFlow().map { list ->
            SpendingPlanList(
                list.map {
                    SpendingPlan(
                        id = it.id,
                        title = it.title,
                        type = it.type,
                        amount = it.amount,
                        planDate = it.planDate,
                        executeDate = it.executeDate,
                        isExecuted = it.isExecuted,
                        willExecute = it.willExecute,
                    )
                }
            )
        }
    }

    override fun getSpendingPlansByMonth(
        date: LocalDate
    ): Flow<SpendingPlanList> {
        return spendingPlanDao.getSpendingPlansByMonth(
            date.year.toString(),
            date.monthValue.toString()
        ).map { list ->
            SpendingPlanList(
                list.map {
                    SpendingPlan(
                        id = it.id,
                        title = it.title,
                        type = it.type,
                        amount = it.amount,
                        planDate = it.planDate,
                        executeDate = it.executeDate,
                        isExecuted = it.isExecuted,
                        willExecute = it.willExecute,
                    )
                }
            )
        }
    }

    override suspend fun updateExecuteState(id: Long, executeDate: LocalDate, isExecuted: Boolean) {
        try {
            spendingPlanDao.updateExecuteState(id, executeDate, isExecuted)
        } catch (e: Exception) {
            Logger.e("updateExecuteState error: $e")
        }
    }

    override suspend fun updateWillExecuteState(id: Long, willExecute: Boolean) {
        try {
            spendingPlanDao.updateWillExecuteState(id, willExecute)
        } catch (e: Exception) {
            Logger.e("updateWillExecuteState error: $e")
        }
    }

    override suspend fun deleteById(id: Long) {
        try {
            spendingPlanDao.deleteById(id)
        } catch (e: Exception) {
            Logger.e("deleteById error: $e")
        }
    }
}