package jun.money.mate.domain

import jun.money.mate.dataApi.database.ChallengeRepository
import jun.money.mate.dataApi.database.IncomeRepository
import jun.money.mate.dataApi.database.SaveRepository
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.save.Challenge
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.YearMonth
import javax.inject.Inject

class GetFinanceUsecase @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val saveRepository: SaveRepository,
    private val challengeRepository: ChallengeRepository,
) {

    operator fun invoke(
        month: YearMonth = YearMonth.now(),
    ): Flow<FinanceData> {
        return combine(
            incomeRepository.getIncomesByMonth(month),
            saveRepository.getSavingFlow(month),
            challengeRepository.getChallengeList(),
        ) { incomes, savingPlans, challenge ->
            Logger.d("incomes: $incomes")
            Logger.d("savingPlans: $savingPlans")
            Logger.d("challenge: $challenge")
            FinanceData(
                incomeList = incomes,
                savePlanList = savingPlans,
                challenge = challenge,
            )
        }
    }
}

data class FinanceData(
    val incomeList: IncomeList,
    val savePlanList: SavePlanList,
    val challenge: List<Challenge>,
)
