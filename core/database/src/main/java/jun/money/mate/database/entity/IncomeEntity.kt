package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import jun.money.mate.model.income.IncomeType
import java.time.LocalDate

@Entity(tableName = AppDatabase.INCOME_TABLE_NAME)
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val type: IncomeType,
    val incomeDate: LocalDate,
)