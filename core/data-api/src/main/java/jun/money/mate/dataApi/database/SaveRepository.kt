package jun.money.mate.dataApi.database

import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavePlanList
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface SaveRepository {

    suspend fun upsertSavePlan(savePlan: SavePlan)

    fun getSavePlanListFlow(): Flow<SavePlanList>

    fun getSavingFlow(date: YearMonth): Flow<SavePlanList>

    suspend fun getSavePlanListByMonth(date: YearMonth): SavePlanList

    suspend fun getSavingByParentId(id: Long): List<SavePlan>

    fun getSavePlan(id: Long): Flow<SavePlan>

    suspend fun updateExecuteState(id: Long, isExecuted: Boolean)

    suspend fun deleteById(id: Long)

    suspend fun deleteByIds(ids: List<Long>)
}
