package jun.money.mate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jun.money.mate.database.converter.IncomeTypeConverter
import jun.money.mate.database.converter.LocalDateConverter
import jun.money.mate.database.converter.LocalDateTimeConverter
import jun.money.mate.database.converter.SpendingTypeConverter
import jun.money.mate.database.dao.IncomeDao
import jun.money.mate.database.dao.SavingPlanDao
import jun.money.mate.database.dao.SpendingPlanDao
import jun.money.mate.database.entity.IncomeEntity
import jun.money.mate.database.entity.SavingPlanEntity
import jun.money.mate.database.entity.SpendingPlanEntity

@Database(
    entities = [
        SpendingPlanEntity::class,
        SavingPlanEntity::class,
        IncomeEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        LocalDateTimeConverter::class,
        LocalDateConverter::class,
        SpendingTypeConverter::class,
        IncomeTypeConverter::class,
    ]
)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun spendingPlanDao(): SpendingPlanDao
    abstract fun savingPlanDao(): SavingPlanDao
    abstract fun incomeDao(): IncomeDao

    companion object {
        const val DATABASE_NAME = "app_database"

        const val INCOME_TABLE_NAME = "income"
        const val SPENDING_PLAN_TABLE_NAME = "spending_plan"
        const val SAVING_PLAN_TABLE_NAME = "saving_plan"
    }
}