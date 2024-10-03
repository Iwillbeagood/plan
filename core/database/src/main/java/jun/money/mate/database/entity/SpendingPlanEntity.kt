package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import jun.money.mate.model.spending.SpendingType
import java.time.LocalDate

@Entity(tableName = AppDatabase.SPENDING_PLAN_TABLE_NAME)
data class SpendingPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val type: SpendingType,
    val amount: Double,
    val planDate: LocalDate,
    val executeDate: LocalDate,
    val isExecuted: Boolean = false,
    val willExecute: Boolean = true
)