package jun.money.mate.dataApi.database

import jun.money.mate.model.spending.Cost
import kotlinx.coroutines.flow.Flow

interface CostRepository {

    suspend fun upsertCost(cost: Cost)

    fun getCostFlow(): Flow<List<Cost>>

    suspend fun getCostById(
        id: Long,
    ): Cost

    suspend fun deleteById(id: Long)

    suspend fun deleteByIds(ids: List<Long>)
}
