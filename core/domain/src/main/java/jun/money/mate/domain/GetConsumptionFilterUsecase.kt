package jun.money.mate.domain

import jun.money.mate.data_api.database.SpendingPlanRepository
import jun.money.mate.model.consumption.ConsumptionFilter
import jun.money.mate.model.consumption.ConsumptionFilter.Companion.toConsumptionFilter
import jun.money.mate.model.spending.SpendingPlanList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetConsumptionFilterUsecase @Inject constructor(
    private val spendingPlanRepository: SpendingPlanRepository
) {

    suspend operator fun invoke(): List<ConsumptionFilter> {
        return spendingPlanRepository.getSpendingPlan().toConsumptionFilter()
    }
}