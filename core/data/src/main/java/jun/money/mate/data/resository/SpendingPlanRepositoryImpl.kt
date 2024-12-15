package jun.money.mate.data.resository

import jun.money.mate.data.mapper.toSpendingPlan
import jun.money.mate.data_api.database.SpendingPlanRepository
import jun.money.mate.database.dao.SpendingPlanDao
import jun.money.mate.database.entity.SpendingPlanEntity
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingPlanList
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SpendingPlanRepositoryImpl @Inject constructor(
    private val spendingPlanDao: SpendingPlanDao
) : SpendingPlanRepository {

    override suspend fun upsertSpendingPlan(
        spendingPlan: SpendingPlan,
        onError: (MessageType) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            spendingPlanDao.upsertSpendingPlan(
                SpendingPlanEntity(
                    id = spendingPlan.id,
                    title = spendingPlan.title,
                    type = spendingPlan.type,
                    spendingCategory = spendingPlan.spendingCategory,
                    amount = spendingPlan.amount,
                    planDay = spendingPlan.planDay,
                    isApply = spendingPlan.isApply,
                )
            )

            onSuccess()
        } catch (e: Exception) {
            Logger.e("upsertSpendingPlan error: $e")
        }
    }

    override fun getSpendingPlanFlow(): Flow<SpendingPlanList> {
        return spendingPlanDao.getSpendingPlanFlow().map { list ->
            SpendingPlanList(
                list.map {
                    it.toSpendingPlan()
                }
            )
        }
    }

    override suspend fun getSpendingPlan(): SpendingPlanList {
        return SpendingPlanList(
            spendingPlans = spendingPlanDao.getSpendingPlan().map {
                it.toSpendingPlan()
            }
        )
    }

    override suspend fun getSpendingPlanById(id: Long): SpendingPlan {
        return spendingPlanDao.getSpendingPlanById(id).toSpendingPlan()
    }

    override suspend fun updateApplyingState(id: Long, isApply: Boolean) {
        try {
            spendingPlanDao.updateApplyingState(id, isApply)
        } catch (e: Exception) {
            Logger.e("updateExecuteState error: $e")
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