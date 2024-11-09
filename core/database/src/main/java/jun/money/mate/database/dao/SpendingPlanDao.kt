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


    @Query("SELECT * FROM $SPENDING_PLAN_TABLE_NAME")
    suspend fun getSpendingPlan(): List<SpendingPlanEntity>

    @Query("SELECT * FROM $SPENDING_PLAN_TABLE_NAME WHERE id = :id")
    suspend fun getSpendingPlanById(id: Long): SpendingPlanEntity

    @Query("SELECT * FROM $SPENDING_PLAN_TABLE_NAME WHERE strftime('%Y', planDate) = :year AND strftime('%m', planDate) = :month")
    fun getSpendingPlansByMonth(year: String, month: String): Flow<List<SpendingPlanEntity>>

    @Query("DELETE FROM $SPENDING_PLAN_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE $SPENDING_PLAN_TABLE_NAME SET isApply = :isApply WHERE id = :id")
    suspend fun updateApplyingState(id: Long, isApply: Boolean)
}