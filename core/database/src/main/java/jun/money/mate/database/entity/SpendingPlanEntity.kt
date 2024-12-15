package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import jun.money.mate.database.util.generateInsertQuery
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingCategoryType
import jun.money.mate.model.spending.SpendingType

@Entity(
    tableName = AppDatabase.SPENDING_PLAN_TABLE_NAME,
    indices = [Index(value = ["title"], unique = true)]
)
data class SpendingPlanEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val type: SpendingType,
    val spendingCategory: SpendingCategory,
    val amount: Long,
    val planDay: Int,
    val isApply: Boolean = false
) {

    companion object {
        private val DEFAULT_SPENDING_PLAN = SpendingPlanEntity(
            id = System.currentTimeMillis(),
            title = "생활비",
            type = SpendingType.ConsumptionPlan,
            spendingCategory = SpendingCategory.ETC,
            amount = 0,
            planDay = 1,
            isApply = false
        )

        val INSERT_QUERY = generateInsertQuery(DEFAULT_SPENDING_PLAN, AppDatabase.SPENDING_PLAN_TABLE_NAME)
    }
}