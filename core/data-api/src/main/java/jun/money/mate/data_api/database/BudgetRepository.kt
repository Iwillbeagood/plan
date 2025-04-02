package jun.money.mate.data_api.database

import jun.money.mate.model.consumption.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    suspend fun upsert(budget: Budget)

    fun getBudgetsFlow(): Flow<List<Budget>>

    suspend fun getBudget(
        id: Long
    ): Budget

    suspend fun deleteBudget(id: Long)
}