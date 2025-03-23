package jun.money.mate.data_api.database

import jun.money.mate.model.spending.Cost
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface CostRepository {

    suspend fun upsertCost(cost: Cost)

    fun getCostFlow(): Flow<List<Cost>>

    suspend fun getCostById(
        id: Long
    ): Cost

    fun getCostsByMonth(
        data: YearMonth = YearMonth.now()
    ): Flow<List<Cost>>

    suspend fun deleteById(id: Long)

    suspend fun deleteByIds(ids: List<Long>)
}