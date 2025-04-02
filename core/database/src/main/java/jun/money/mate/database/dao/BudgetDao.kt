package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase.Companion.BUDGET_TABLE_NAME
import jun.money.mate.database.entity.BudgetEntity
import jun.money.mate.database.entity.BudgetWithUsed
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Upsert
    suspend fun upsert(entity: BudgetEntity)

    @Transaction
    @Query("SELECT * FROM $BUDGET_TABLE_NAME")
    fun getListFlow(): Flow<List<BudgetWithUsed>>

    @Transaction
    @Query("SELECT * FROM $BUDGET_TABLE_NAME WHERE id = :id")
    suspend fun get(id: Long): BudgetWithUsed

    @Query("DELETE FROM $BUDGET_TABLE_NAME WHERE id = :id")
    suspend fun delete(id: Long)
}