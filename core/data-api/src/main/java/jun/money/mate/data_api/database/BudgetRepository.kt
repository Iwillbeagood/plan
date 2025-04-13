package jun.money.mate.data_api.database

import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.consumption.PastBudget
import jun.money.mate.model.consumption.Used
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    suspend fun upsert(budget: Budget)

    suspend fun insertUsed(used: Used)

    suspend fun insertPastBudget(pastBudget: PastBudget, budgetId: Long)

    fun getBudgetsFlow(): Flow<List<Budget>>

    suspend fun getBudget(id: Long): Budget

    fun getBudgetFlow(id: Long): Flow<Budget>

    suspend fun updateUsed(used: Used)

    suspend fun deleteBudget(id: Long)

    suspend fun deleteUsed(vararg id: Long)

    suspend fun resetBudgetUsed(budgetId: Long)
}