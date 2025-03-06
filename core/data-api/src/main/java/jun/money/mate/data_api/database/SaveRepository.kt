package jun.money.mate.data_api.database

import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavePlanList
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SaveRepository {

    suspend fun upsertSavePlan(savePlan: SavePlan)

    fun getSavePlanListFlow(): Flow<SavePlanList>

    suspend fun getSavePlan(id: Long): SavePlan

    suspend fun updateExecuteState(id: Long, isExecuted: Boolean)

    suspend fun deleteById(id: Long)

    suspend fun deleteByIds(ids: List<Long>)
}