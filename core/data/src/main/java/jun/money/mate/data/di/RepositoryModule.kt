package jun.money.mate.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jun.money.mate.data.resository.IncomeRepositoryImpl
import jun.money.mate.data.resository.SavingPlanRepositoryImpl
import jun.money.mate.data.resository.SpendingPlanRepositoryImpl
import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.data_api.database.SavingPlanRepository
import jun.money.mate.data_api.database.SpendingPlanRepository

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindsIncomeRepository(
        repository: IncomeRepositoryImpl
    ): IncomeRepository

    @Binds
    abstract fun bindsSavingPlanRepository(
        repository: SavingPlanRepositoryImpl
    ): SavingPlanRepository

    @Binds
    abstract fun bindsSpendingPlanRepository(
        repository: SpendingPlanRepositoryImpl
    ): SpendingPlanRepository
}
