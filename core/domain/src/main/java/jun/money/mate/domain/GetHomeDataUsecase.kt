package jun.money.mate.domain

import jun.money.mate.data_api.database.BudgetRepository
import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.save.SavePlanList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHomeDataUsecase @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val saveRepository: SaveRepository,
    private val budgetRepository: BudgetRepository
) {

    operator fun invoke(): Flow<HomeData> {
        return combine(
            incomeRepository.getIncomesByMonth(),
            saveRepository.getSavePlanListFlow(),
            budgetRepository.getBudgetsFlow()
        ) { incomes, savingPlans, budgets ->
            HomeData(
                incomeList = incomes,
                savePlanList = savingPlans,
                budgets = budgets
            )
        }
    }
}

data class HomeData(
    val incomeList: IncomeList,
    val savePlanList: SavePlanList,
    val budgets: List<Budget>
)