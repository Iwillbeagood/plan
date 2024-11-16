package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase.Companion.CONSUMPTION_TABLE_NAME
import jun.money.mate.database.AppDatabase.Companion.INCOME_TABLE_NAME
import jun.money.mate.database.entity.ConsumptionEntity
import jun.money.mate.database.entity.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumptionDao {

    @Upsert
    suspend fun upsertConsumption(entity: ConsumptionEntity)

    @Query("SELECT * FROM $CONSUMPTION_TABLE_NAME")
    fun getConsumptionFlow(): Flow<List<ConsumptionEntity>>

    @Query("SELECT * FROM $CONSUMPTION_TABLE_NAME")
    suspend fun getConsumptionList(): List<ConsumptionEntity>

    @Query("SELECT * FROM $CONSUMPTION_TABLE_NAME WHERE id = :id")
    suspend fun getConsumptionById(id: Long): ConsumptionEntity

    @Query("SELECT * FROM $CONSUMPTION_TABLE_NAME WHERE strftime('%Y', date) = :year AND strftime('%m', date) = :month")
    fun getConsumptionByMonth(year: String, month: String): Flow<List<ConsumptionEntity>>


    @Query("DELETE FROM $CONSUMPTION_TABLE_NAME WHERE id = :id")
    suspend fun deleteConsumptionById(id: Long)

    @Query("DELETE FROM $CONSUMPTION_TABLE_NAME")
    suspend fun deleteAllConsumption()
}