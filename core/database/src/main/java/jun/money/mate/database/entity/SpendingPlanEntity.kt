package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import jun.money.mate.model.spending.SpendingType
import java.time.LocalDate

@Entity(tableName = AppDatabase.SPENDING_PLAN_TABLE_NAME)
data class SpendingPlanEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val type: SpendingType,
    val spendingCategoryName: String,
    val amount: Long,
    val planDate: LocalDate,
    val isApply: Boolean = false
)