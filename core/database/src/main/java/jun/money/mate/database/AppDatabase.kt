package jun.money.mate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jun.money.mate.database.converter.ChallengeTypeConverter
import jun.money.mate.database.converter.DateTypeConverter
import jun.money.mate.database.converter.LocalDateConverter
import jun.money.mate.database.converter.LocalDateTimeConverter
import jun.money.mate.database.converter.SaveCategoryConverter
import jun.money.mate.database.converter.SpendingCategoryConverter
import jun.money.mate.database.converter.SpendingTypeConverter
import jun.money.mate.database.converter.YearMonthConverter
import jun.money.mate.database.dao.ChallengeDao
import jun.money.mate.database.dao.ConsumptionDao
import jun.money.mate.database.dao.IncomeDao
import jun.money.mate.database.dao.SaveDao
import jun.money.mate.database.dao.SpendingPlanDao
import jun.money.mate.database.entity.ChallengeEntity
import jun.money.mate.database.entity.ChallengeProgressEntity
import jun.money.mate.database.entity.ChallengeWithProgress
import jun.money.mate.database.entity.ConsumptionEntity
import jun.money.mate.database.entity.IncomeEntity
import jun.money.mate.database.entity.SaveEntity
import jun.money.mate.database.entity.SpendingPlanEntity

@Database(
    entities = [
        SpendingPlanEntity::class,
        SaveEntity::class,
        IncomeEntity::class,
        ConsumptionEntity::class,
        ChallengeEntity::class,
        ChallengeProgressEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        LocalDateTimeConverter::class,
        LocalDateConverter::class,
        SpendingTypeConverter::class,
        SpendingCategoryConverter::class,
        SaveCategoryConverter::class,
        DateTypeConverter::class,
        YearMonthConverter::class,
        ChallengeTypeConverter::class,
    ]
)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun spendingPlanDao(): SpendingPlanDao
    abstract fun savingPlanDao(): SaveDao
    abstract fun incomeDao(): IncomeDao
    abstract fun consumptionDao(): ConsumptionDao
    abstract fun challengeDao(): ChallengeDao

    companion object {
        const val DATABASE_NAME = "app_database"

        const val INCOME_TABLE_NAME = "income"
        const val SPENDING_PLAN_TABLE_NAME = "spending_plan"
        const val SAVING_PLAN_TABLE_NAME = "saving_plan"
        const val CONSUMPTION_TABLE_NAME = "consumption"
        const val CHALLENGE_TABLE_NAME = "challenge"
        const val CHALLENGE_PROGRESS_TABLE_NAME = "challenge_progress"
    }
}