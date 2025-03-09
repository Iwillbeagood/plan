package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import jun.money.mate.model.save.SavingsType
import java.time.LocalDate
import java.time.YearMonth

@Entity(tableName = AppDatabase.SAVING_PLAN_TABLE_NAME)
data class SaveEntity(
    @PrimaryKey val id: Long,
    val parentId: Long,
    val amount: Long,
    val day: Int,
    val addYearMonth: YearMonth,
    val savingsType: SavingsType,
    val executed: Boolean,
)