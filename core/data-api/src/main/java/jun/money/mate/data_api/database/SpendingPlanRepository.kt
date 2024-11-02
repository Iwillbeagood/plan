package jun.money.mate.data_api.database

import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingPlanList
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SpendingPlanRepository {

    suspend fun upsertSpendingPlan(spendingPlan: SpendingPlan)

    fun getSpendingPlanFlow(): Flow<SpendingPlanList>

    suspend fun getSpendingPlanById(id: Long): SpendingPlan

    fun getSpendingPlansByMonth(
        date: LocalDate = LocalDate.now()
    ): Flow<SpendingPlanList>

    suspend fun updateApplyingState(id: Long, isApply: Boolean)

    suspend fun deleteById(id: Long)
}