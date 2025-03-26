package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase.Companion.COST_TABLE_NAME
import jun.money.mate.database.entity.CostEntity
import jun.money.mate.database.entity.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CostDao {

    @Upsert
    suspend fun upsertCost(entity: CostEntity)

    @Query("SELECT * FROM $COST_TABLE_NAME")
    fun getCostFlow(): Flow<List<CostEntity>>

    @Query("SELECT * FROM $COST_TABLE_NAME")
    suspend fun getCosts(): List<CostEntity>

    @Query("SELECT * FROM $COST_TABLE_NAME WHERE id = :costId")
    suspend fun getCostById(costId: Long): CostEntity

    @Query("DELETE FROM $COST_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM $COST_TABLE_NAME WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)
}