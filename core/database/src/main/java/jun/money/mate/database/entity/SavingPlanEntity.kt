package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import java.time.LocalDate

@Entity(tableName = AppDatabase.SAVING_PLAN_TABLE_NAME)
data class SavingPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val amount: Double,
    val planDate: LocalDate,
    val executeDate: LocalDate,
    val isExecuted: Boolean = false,
    val willExecute: Boolean = true
)