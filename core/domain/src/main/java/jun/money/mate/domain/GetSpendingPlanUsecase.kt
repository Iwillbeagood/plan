package jun.money.mate.domain

import jun.money.mate.data_api.database.ConsumptionRepository
import jun.money.mate.data_api.database.SpendingPlanRepository
import jun.money.mate.model.spending.ConsumptionSpend
import jun.money.mate.model.spending.SpendingPlanList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class GetSpendingPlanUsecase @Inject constructor(
    private val spendingPlanRepository: SpendingPlanRepository,
    private val consumptionRepository: ConsumptionRepository
) {

    operator fun invoke(date: LocalDate): Flow<SpendingPlanData> {
        return combine(
            spendingPlanRepository.getSpendingPlansByMonth(date),
            consumptionRepository.getConsumptionByMonth(date)
        ) { spendingPlanList, consumptionList ->
            SpendingPlanData(
                spendingPlanList,
                spendingPlanList.consumptionPlan.map {
                    ConsumptionSpend(
                        it,
                        consumptionList.consumptions.filter { consumption ->
                            consumption.planTitle == it.title
                        }.sumOf { it.amount })
                }
            )
        }
    }
}

data class SpendingPlanData(
    val spendingPlanList: SpendingPlanList,
    val consumptionPlan: List<ConsumptionSpend>,
)