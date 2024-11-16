package jun.money.mate.domain

import jun.money.mate.data_api.database.SpendingPlanRepository
import javax.inject.Inject

class GetSpendingConsumptionTitlesUsecase @Inject constructor(
    private val spendingPlanRepository: SpendingPlanRepository
) {

    suspend operator fun invoke(): List<String> {
        return spendingPlanRepository.getSpendingPlan().consumptionPlan.map { it.title }
    }
}