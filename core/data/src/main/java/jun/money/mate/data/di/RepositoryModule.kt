package jun.money.mate.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jun.money.mate.data.resository.BudgetRepositoryImpl
import jun.money.mate.data.resository.ChallengeRepositoryImpl
import jun.money.mate.data.resository.CostRepositoryImpl
import jun.money.mate.data.resository.IncomeRepositoryImpl
import jun.money.mate.data.resository.SaveRepositoryImpl
import jun.money.mate.dataApi.database.BudgetRepository
import jun.money.mate.dataApi.database.ChallengeRepository
import jun.money.mate.dataApi.database.CostRepository
import jun.money.mate.dataApi.database.IncomeRepository
import jun.money.mate.dataApi.database.SaveRepository

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindsIncomeRepository(
        repository: IncomeRepositoryImpl,
    ): IncomeRepository

    @Binds
    abstract fun bindsSavingPlanRepository(
        repository: SaveRepositoryImpl,
    ): SaveRepository

    @Binds
    abstract fun bindsBudgetRepository(
        repository: BudgetRepositoryImpl,
    ): BudgetRepository

    @Binds
    abstract fun bindsChallengeRepository(
        repository: ChallengeRepositoryImpl,
    ): ChallengeRepository

    @Binds
    abstract fun bindsCostRepository(
        repository: CostRepositoryImpl,
    ): CostRepository
}
