package jun.money.mate.ui.interop

import androidx.compose.runtime.staticCompositionLocalOf
import jun.money.mate.navigation.MainBottomNavItem
import java.time.YearMonth

val LocalNavigateActionInterop = staticCompositionLocalOf<NavigateActionInterop> {
    error("No NavigateActionInterop provided")
}

interface NavigateActionInterop {
    fun popBackStack()
    fun navigateBottomNav(item: MainBottomNavItem)
    fun navigateToIncomeList(date: YearMonth)
    fun navigateToSaveList(date: YearMonth)
    fun navigateToIncomeAdd()
    fun navigateToIncomeEdit(id: Long)
    fun navigateToSpendingPlanAdd()
    fun navigateToConsumptionAdd()
    fun navigateToConsumptionEdit(id: Long)
    fun navigateToSpendingPlanEdit(id: Long)
    fun navigateToSavingAdd()
    fun navigateToSavingDetail(id: Long)
    fun navigateToChallengeAdd()
    fun navigateToChallengeDetail(id: Long)
}