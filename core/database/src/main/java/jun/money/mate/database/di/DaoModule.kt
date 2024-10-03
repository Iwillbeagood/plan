package jun.money.mate.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jun.money.mate.database.AppDatabase
import jun.money.mate.database.dao.IncomeDao
import jun.money.mate.database.dao.SavingPlanDao
import jun.money.mate.database.dao.SpendingPlanDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    @Singleton
    fun provideSpendingDao(
        database: AppDatabase,
    ) : SpendingPlanDao = database.spendingPlanDao()

    @Provides
    @Singleton
    fun provideSavingPlanDao(
        database: AppDatabase,
    ) : SavingPlanDao = database.savingPlanDao()

    @Provides
    @Singleton
    fun provideIncomeDao(
        database: AppDatabase,
    ) : IncomeDao = database.incomeDao()
}
