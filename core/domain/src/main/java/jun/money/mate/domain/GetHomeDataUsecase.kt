package jun.money.mate.domain

import jun.money.mate.data_api.database.ConsumptionRepository
import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.data_api.database.SpendingPlanRepository
import jun.money.mate.model.consumption.ConsumptionList
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.model.spending.SpendingPlanList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHomeDataUsecase @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val saveRepository: SaveRepository,
    private val spendingPlanRepository: SpendingPlanRepository,
    private val consumptionRepository: ConsumptionRepository
) {

    operator fun invoke(): Flow<HomeData> {
        return combine(
            incomeRepository.getIncomesByMonth(),
            saveRepository.getSavePlanListFlow(),
            spendingPlanRepository.getSpendingPlanFlow(),
            consumptionRepository.getConsumptionFlow()
        ) { incomes, savingPlans, spendingPlans, consumptionList ->
            HomeData(
                incomeList = incomes,
                savePlanList = savingPlans,
                spendingPlanList = spendingPlans,
                consumptionList = consumptionList
            )
        }
    }
}

data class HomeData(
    val incomeList: IncomeList,
    val savePlanList: SavePlanList,
    val spendingPlanList: SpendingPlanList,
    val consumptionList: ConsumptionList
)