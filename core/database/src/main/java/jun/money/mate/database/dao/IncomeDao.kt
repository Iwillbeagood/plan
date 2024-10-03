package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase.Companion.INCOME_TABLE_NAME
import jun.money.mate.database.entity.IncomeEntity

@Dao
interface IncomeDao {

    @Upsert
    suspend fun upsertIncome(entity: IncomeEntity)

    @Query("DELETE FROM $INCOME_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: Long)
}