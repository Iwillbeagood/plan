package jun.money.mate.data_api.database

import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingPlanList
import kotlinx.coroutines.flow.Flow

interface SpendingPlanRepository {

    suspend fun upsertSpendingPlan(spendingPlan: SpendingPlan)

    fun getSpendingPlanFlow(): Flow<SpendingPlanList>

    suspend fun getSpendingPlan(): SpendingPlanList

    suspend fun getSpendingPlanById(id: Long): SpendingPlan

    suspend fun updateApplyingState(id: Long, isApply: Boolean)

    suspend fun deleteById(id: Long)
}