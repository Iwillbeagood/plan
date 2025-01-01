package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase.Companion.SAVING_PLAN_TABLE_NAME
import jun.money.mate.database.entity.SaveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaveDao {

    @Upsert
    suspend fun upsert(entity: SaveEntity)

    @Query("SELECT * FROM $SAVING_PLAN_TABLE_NAME")
    fun getFlow(): Flow<List<SaveEntity>>

    @Query("SELECT * FROM $SAVING_PLAN_TABLE_NAME WHERE id = :id")
    suspend fun get(id: Long): SaveEntity

    @Query("UPDATE $SAVING_PLAN_TABLE_NAME SET executeMonth = :executeMonth, executed = :isExecuted WHERE id = :id")
    suspend fun updateExecuteState(id: Long, executeMonth: Int, isExecuted: Boolean)

    @Query(
        """
        UPDATE $SAVING_PLAN_TABLE_NAME
        SET executed = 0
        WHERE executeMonth != :currentMonth
    """
    )
    suspend fun resetExecutedState(currentMonth: Int)

    @Query("DELETE FROM $SAVING_PLAN_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: Long)
}