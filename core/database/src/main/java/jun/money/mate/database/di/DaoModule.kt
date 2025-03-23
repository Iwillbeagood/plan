package jun.money.mate.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jun.money.mate.database.AppDatabase
import jun.money.mate.database.dao.ChallengeDao
import jun.money.mate.database.dao.ConsumptionDao
import jun.money.mate.database.dao.CostDao
import jun.money.mate.database.dao.IncomeDao
import jun.money.mate.database.dao.SaveDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    @Singleton
    fun provideSavingPlanDao(
        database: AppDatabase,
    ) : SaveDao = database.savingPlanDao()

    @Provides
    @Singleton
    fun provideIncomeDao(
        database: AppDatabase,
    ) : IncomeDao = database.incomeDao()

    @Provides
    @Singleton
    fun provideConsumptionDao(
        database: AppDatabase,
    ) : ConsumptionDao = database.consumptionDao()

    @Provides
    @Singleton
    fun provideChallengeDao(
        database: AppDatabase,
    ) : ChallengeDao = database.challengeDao()

    @Provides
    @Singleton
    fun provideCostDao(
        database: AppDatabase,
    ) : CostDao = database.costDao()
}
