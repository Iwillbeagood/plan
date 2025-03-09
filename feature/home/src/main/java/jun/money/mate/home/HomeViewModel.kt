package jun.money.mate.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.domain.GetHomeDataUsecase
import jun.money.mate.model.consumption.ConsumptionList
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.model.spending.SpendingPlanList
import jun.money.mate.navigation.MainBottomNavItem
import jun.money.mate.utils.currency.CurrencyFormatter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    getHomeDataUsecase: GetHomeDataUsecase
) : ViewModel() {

    val homeState: StateFlow<HomeState> = getHomeDataUsecase().map {
        HomeState.HomeData(
            incomeList = it.incomeList,
            savePlanList = it.savePlanList,
            spendingPlanList = it.spendingPlanList,
            consumptionList = it.consumptionList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeState.Loading
    )

    private val _homeEffect = MutableSharedFlow<HomeEffect>()
    val homeEffect: SharedFlow<HomeEffect> get() = _homeEffect.asSharedFlow()

    fun navigateTo(navItem: MainBottomNavItem) {
        viewModelScope.launch {
            _homeEffect.emit(HomeEffect.ShowMainNavScreen(navItem))
        }
    }

    fun navigateToAdd(navItem: MainBottomNavItem) {
        navigateTo(navItem)

    }
}

@Stable
internal sealed interface HomeState {

    @Immutable
    data object Loading : HomeState

    @Immutable
    data class HomeData(
        val incomeList: IncomeList,
        val savePlanList: SavePlanList,
        val spendingPlanList: SpendingPlanList,
        val consumptionList: ConsumptionList
    ) : HomeState {


        val balance get() = incomeList.total - savePlanList.executedTotal - spendingPlanList.total
        val balanceString get() = CurrencyFormatter.formatAmountWon(balance)

        data class HomeList(
            val value: String,
            val type: MainBottomNavItem
        )
    }
}

@Stable
internal sealed interface HomeEffect {

    @Immutable
    data class ShowMainNavScreen(val navItem: MainBottomNavItem) : HomeEffect

    @Immutable
    data object ShowIncomeAddScreen : HomeEffect

    @Immutable
    data object ShowConsumptionAddScreen : HomeEffect

    @Immutable
    data object ShowSpendingAddScreen : HomeEffect

    @Immutable
    data object ShowSaveAddScreen : HomeEffect

}