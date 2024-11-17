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
import jun.money.mate.designsystem.theme.Yellow1
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.saving.SavingPlanList
import jun.money.mate.model.spending.SpendingPlanList
import jun.money.mate.utils.currency.CurrencyFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
        spendingPlanRepository.getSpendingPlanFlow()
    ) { incomes, savingPlans, spendingPlans ->
        HomeState.HomeData(
            incomeList = incomes,
            savingPlanList = savingPlans,
            spendingPlanList = spendingPlans
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeState.Loading
    )

    private val _homeEffect = MutableSharedFlow<HomeEffect>()
    val homeEffect: SharedFlow<HomeEffect> get() = _homeEffect.asSharedFlow()

    private var contentJob: Job? = null

    fun showIncomeScreen() {
        val state = homeState.value as? HomeState.HomeData ?: return

        viewModelScope.launch {
            if (state.incomeList.incomes.isEmpty()) {
                _homeEffect.emit(HomeEffect.ShowIncomeAddScreen)
            } else {
                _homeEffect.emit(HomeEffect.ShowIncomeListScreen)
            }
        }
    }

    fun showSpendingScreen() {
        val state = homeState.value as? HomeState.HomeData ?: return

        viewModelScope.launch {
            if (state.spendingPlanList.spendingPlans.isEmpty()) {
                _homeEffect.emit(HomeEffect.ShowSpendingAddScreen)
            } else {
                _homeEffect.emit(HomeEffect.ShowSpendingListScreen)
            }
        }
    }
}

@Stable
internal sealed interface HomeState {

    @Immutable
    data object Loading : HomeState

    @Immutable
    data class HomeData(
        val incomeList: IncomeList,
        val savingPlanList: SavingPlanList,
        val spendingPlanList: SpendingPlanList
    ) : HomeState {

        val balance get() =  incomeList.total - spendingPlanList.total
        val balanceString get() = CurrencyFormatter.formatAmountWon(balance)

        val isShowPieChart get() = spendingPlanList.total > 0

        val pieList: List<Pie>
            get() = listOf(
                Pie(label = "정기 지출", data = spendingPlanList.predictTotal.toDouble(), color = Yellow1),
            )
    }
}

@Stable
internal sealed interface HomeEffect {

    @Immutable
    data object ShowIncomeListScreen : HomeEffect

    @Immutable
    data object ShowIncomeAddScreen : HomeEffect

    @Immutable
    data object ShowSpendingPlanListScreen : HomeEffect

    @Immutable
    data object ShowSpendingListScreen : HomeEffect

    @Immutable
    data object ShowSpendingAddScreen : HomeEffect
}