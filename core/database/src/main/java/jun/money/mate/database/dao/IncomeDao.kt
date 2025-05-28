package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import jun.money.mate.database.AppDatabase.Companion.INCOME_TABLE_NAME
import jun.money.mate.database.entity.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Insert
    suspend fun insertIncome(entity: IncomeEntity)

    @Update
    suspend fun updateIncome(entity: IncomeEntity)

    @Query("SELECT * FROM $INCOME_TABLE_NAME")
    fun getIncomeFlow(): Flow<List<IncomeEntity>>

    @Query("SELECT * FROM $INCOME_TABLE_NAME WHERE id = :incomeId")
    suspend fun getIncomeById(incomeId: Long): IncomeEntity

    @Query("DELETE FROM $INCOME_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM $INCOME_TABLE_NAME WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)
}
