package jun.money.mate.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ehsannarmani.compose_charts.models.Pie
import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.data_api.database.SavingPlanRepository
import jun.money.mate.data_api.database.SpendingPlanRepository
import jun.money.mate.designsystem.theme.Green2
import jun.money.mate.designsystem.theme.Yellow1
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.saving.SavingPlanList
import jun.money.mate.model.spending.SpendingPlanList
import jun.money.mate.utils.currency.CurrencyFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    incomeRepository: IncomeRepository,
    savingPlanRepository: SavingPlanRepository,
    spendingPlanRepository: SpendingPlanRepository
) : ViewModel() {

    val homeState: StateFlow<HomeState> = combine(
        incomeRepository.getIncomesByMonth(),
        savingPlanRepository.getSavingPlansByMonth(),
        spendingPlanRepository.getSpendingPlansByMonth()
    ) { incomes, savingPlans, spendingPlans ->
        HomeState.HomeData(
            incomes = incomes,
            savingPlans = savingPlans,
            spendingPlans = spendingPlans
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeState.Loading
    )

    private var contentJob: Job? = null

}

@Stable
internal sealed interface HomeState {

    @Immutable
    data object Loading : HomeState

    @Immutable
    data class HomeData(
        val incomes: IncomeList,
        val savingPlans: SavingPlanList,
        val spendingPlans: SpendingPlanList
    ) : HomeState {

        val balance get() =  incomes.total - spendingPlans.total
        val balanceString get() = CurrencyFormatter.formatAmountWon(balance)

        val isShowPieChart get() = spendingPlans.total > 0

        val pieList: List<Pie>
            get() = listOf(
                Pie(label = "정기 지출", data = spendingPlans.regularTotal, color = Yellow1),
                Pie(label = "변동 지출", data = spendingPlans.variableTotal, color = Green2),
            )
    }
}