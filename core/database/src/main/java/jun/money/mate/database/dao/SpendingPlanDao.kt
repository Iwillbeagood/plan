package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase.Companion.SPENDING_PLAN_TABLE_NAME
import jun.money.mate.database.entity.SpendingPlanEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface SpendingPlanDao {

    @Upsert
    suspend fun upsertSpendingPlan(entity: SpendingPlanEntity)

    @Query("SELECT * FROM $SPENDING_PLAN_TABLE_NAME")
    fun getSpendingPlanFlow(): Flow<List<SpendingPlanEntity>>

    @Query("DELETE FROM $SPENDING_PLAN_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE $SPENDING_PLAN_TABLE_NAME SET executeDate = :executeDate, isExecuted = :isExecuted WHERE id = :id")
    suspend fun updateExecuteState(id: Long, executeDate: LocalDate, isExecuted: Boolean)

    @Query("UPDATE $SPENDING_PLAN_TABLE_NAME SET willExecute = :willExecute WHERE id = :id")
    suspend fun updateWillExecuteState(id: Long, willExecute: Boolean)
}