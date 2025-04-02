package jun.money.mate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jun.money.mate.database.converter.ChallengeTypeConverter
import jun.money.mate.database.converter.CostTypeConverter
import jun.money.mate.database.converter.DateTypeConverter
import jun.money.mate.database.converter.LocalDateConverter
import jun.money.mate.database.converter.LocalDateTimeConverter
import jun.money.mate.database.converter.SaveCategoryConverter
import jun.money.mate.database.converter.YearMonthConverter
import jun.money.mate.database.dao.BudgetDao
import jun.money.mate.database.dao.ChallengeDao
import jun.money.mate.database.dao.CostDao
import jun.money.mate.database.dao.IncomeDao
import jun.money.mate.database.dao.SaveDao
import jun.money.mate.database.entity.BudgetEntity
import jun.money.mate.database.entity.ChallengeEntity
import jun.money.mate.database.entity.ChallengeProgressEntity
import jun.money.mate.database.entity.CostEntity
import jun.money.mate.database.entity.IncomeEntity
import jun.money.mate.database.entity.SaveEntity
import jun.money.mate.database.entity.UsedEntity

@Database(
    entities = [
        SaveEntity::class,
        IncomeEntity::class,
        BudgetEntity::class,
        UsedEntity::class,
        ChallengeEntity::class,
        ChallengeProgressEntity::class,
        CostEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        LocalDateTimeConverter::class,
        LocalDateConverter::class,
        SaveCategoryConverter::class,
        DateTypeConverter::class,
        YearMonthConverter::class,
        ChallengeTypeConverter::class,
        CostTypeConverter::class,
    ]
)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun savingPlanDao(): SaveDao
    abstract fun incomeDao(): IncomeDao
    abstract fun budgetDao(): BudgetDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun costDao(): CostDao

    companion object {
        const val DATABASE_NAME = "app_database"

        const val INCOME_TABLE_NAME = "income"
        const val SAVING_PLAN_TABLE_NAME = "saving_plan"
        const val BUDGET_TABLE_NAME = "budget"
        const val BUDGET_USED_TABLE_NAME = "budget_used"
        const val CHALLENGE_TABLE_NAME = "challenge"
        const val CHALLENGE_PROGRESS_TABLE_NAME = "challenge_progress"
        const val COST_TABLE_NAME = "cost"
    }
}