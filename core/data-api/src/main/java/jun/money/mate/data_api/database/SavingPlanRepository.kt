package jun.money.mate.data_api.database

import jun.money.mate.model.saving.SavingPlan
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SavingPlanRepository {

    suspend fun upsertSavingPlan(savingPlan: SavingPlan)

    fun getSavingPlanFlow(): Flow<List<SavingPlan>>

    suspend fun updateExecuteState(id: Long, executeDate: LocalDate, isExecuted: Boolean)

    suspend fun updateWillExecuteState(id: Long, willExecute: Boolean)

    suspend fun deleteById(id: Long)
}