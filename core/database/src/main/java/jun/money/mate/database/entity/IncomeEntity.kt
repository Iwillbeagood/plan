package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import jun.money.mate.model.etc.DateType
import java.time.LocalDate

@Entity(tableName = AppDatabase.INCOME_TABLE_NAME)
data class IncomeEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val amount: Long,
    val date: Int,
    val addDate: LocalDate,
    val dateType: DateType,
)
