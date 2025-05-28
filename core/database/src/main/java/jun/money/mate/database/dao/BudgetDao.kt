package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase.Companion.BUDGET_TABLE_NAME
import jun.money.mate.database.AppDatabase.Companion.BUDGET_USED_TABLE_NAME
import jun.money.mate.database.entity.BudgetEntity
import jun.money.mate.database.entity.BudgetWithUsed
import jun.money.mate.database.entity.PastBudgetEntity
import jun.money.mate.database.entity.UsedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Upsert
    suspend fun upsert(entity: BudgetEntity)

    @Insert
    suspend fun insertUsed(entity: UsedEntity)

    @Insert
    suspend fun insertPastBudget(entity: PastBudgetEntity)

    @Transaction
    @Query("SELECT * FROM $BUDGET_TABLE_NAME")
    fun getListFlow(): Flow<List<BudgetWithUsed>>

    @Transaction
    @Query("SELECT * FROM $BUDGET_TABLE_NAME WHERE id = :id")
    suspend fun get(id: Long): BudgetWithUsed

    @Transaction
    @Query("SELECT * FROM $BUDGET_TABLE_NAME WHERE id = :id")
    fun getFlow(id: Long): Flow<BudgetWithUsed>

    @Update
    suspend fun updateUsed(entity: UsedEntity)

    @Query("DELETE FROM $BUDGET_TABLE_NAME WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM $BUDGET_USED_TABLE_NAME WHERE id = :id")
    suspend fun deleteUsed(id: Long)

    @Query("DELETE FROM $BUDGET_USED_TABLE_NAME WHERE budgetId = :budgetId")
    suspend fun deleteBudgetUsed(budgetId: Long)
}
