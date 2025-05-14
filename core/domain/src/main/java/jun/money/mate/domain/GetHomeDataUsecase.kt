package jun.money.mate.domain

import jun.money.mate.dataApi.database.BudgetRepository
import jun.money.mate.dataApi.database.CostRepository
import jun.money.mate.dataApi.database.IncomeRepository
import jun.money.mate.dataApi.database.SaveRepository
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.model.spending.Cost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHomeDataUsecase @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val saveRepository: SaveRepository,
    private val budgetRepository: BudgetRepository,
    private val costRepository: CostRepository,
) {

    operator fun invoke(): Flow<HomeData> {
        return combine(
            incomeRepository.getIncomesByMonth(),
            saveRepository.getSavePlanListFlow(),
            budgetRepository.getBudgetsFlow(),
            costRepository.getCostFlow(),
        ) { incomes, savingPlans, budgets, costs ->
            HomeData(
                incomeList = incomes,
                savePlanList = savingPlans,
                budgets = budgets,
                costs = costs,
            )
        }
    }
}

data class HomeData(
    val incomeList: IncomeList,
    val savePlanList: SavePlanList,
    val budgets: List<Budget>,
    val costs: List<Cost>,
)
