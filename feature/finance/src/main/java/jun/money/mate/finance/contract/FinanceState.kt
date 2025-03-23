package jun.money.mate.finance.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.save.Challenge
import jun.money.mate.model.save.SavePlanList

@Stable
internal sealed interface FinanceState {

    @Immutable
    data object Loading : FinanceState

    @Immutable
    data class FinanceData(
        val incomeList: IncomeList,
        val savePlanList: SavePlanList,
        val challengeList: List<Challenge>
    ) : FinanceState
}