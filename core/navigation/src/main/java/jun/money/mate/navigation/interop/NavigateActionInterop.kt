package jun.money.mate.navigation.interop

import androidx.compose.runtime.staticCompositionLocalOf
import jun.money.mate.navigation.MainBottomNavItem
import java.time.YearMonth

val LocalNavigateActionInterop = staticCompositionLocalOf<NavigateActionInterop> {
    error("No NavigateActionInterop provided")
}

interface NavigateActionInterop {
    fun popBackStack()
    fun navigateBottomNav(item: MainBottomNavItem)
    fun navigateToIncomeList()
    fun navigateToSaveList()
    fun navigateToIncomeAdd()
    fun navigateToIncomeEdit(id: Long)
    fun navigateToConsumptionAdd()
    fun navigateToConsumptionEdit(id: Long)
    fun navigateToSavingAdd()
    fun navigateToSavingDetail(id: Long)
    fun navigateToChallengeAdd()
    fun navigateToChallengeDetail(id: Long)
    fun navigateToCostAdd()
    fun navigateToCostDetail(id: Long)
}