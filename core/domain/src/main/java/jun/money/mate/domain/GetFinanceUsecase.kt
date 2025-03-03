package jun.money.mate.domain

import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.save.SavePlanList
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class GetFinanceUsecase @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val saveRepository: SaveRepository
) {

    operator fun invoke(
        month: LocalDate
    ): Flow<Finance> {
        return combine(
            incomeRepository.getIncomesByMonth(month),
            saveRepository.getSavePlanListFlow(),
        ) { incomes, savingPlans ->
            Logger.d("incomes: $incomes, savingPlans: $savingPlans")
            Finance(
                incomeList = incomes,
                savePlanList = savingPlans,
            )
        }
    }
}

data class Finance(
    val incomeList: IncomeList,
    val savePlanList: SavePlanList
)