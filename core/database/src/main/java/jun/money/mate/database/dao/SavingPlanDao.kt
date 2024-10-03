package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase.Companion.SAVING_PLAN_TABLE_NAME
import jun.money.mate.database.entity.SavingPlanEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface SavingPlanDao {

    @Upsert
    suspend fun upsertSavingPlan(entity: SavingPlanEntity)

    @Query("SELECT * FROM $SAVING_PLAN_TABLE_NAME")
    fun getSavingPlanFlow(): Flow<List<SavingPlanEntity>>

    @Query("UPDATE $SAVING_PLAN_TABLE_NAME SET executeDate = :executeDate, isExecuted = :isExecuted WHERE id = :id")
    suspend fun updateExecuteState(id: Long, executeDate: LocalDate, isExecuted: Boolean)

    @Query("UPDATE $SAVING_PLAN_TABLE_NAME SET willExecute = :willExecute WHERE id = :id")
    suspend fun updateWillExecuteState(id: Long, willExecute: Boolean)

    @Query("DELETE FROM $SAVING_PLAN_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: Long)
}