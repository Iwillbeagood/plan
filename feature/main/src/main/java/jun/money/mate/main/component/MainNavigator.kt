package jun.money.mate.main.component

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import jun.money.mate.home.navigation.navigateToHome
import jun.money.mate.income.navigation.navigateToIncomeAdd
import jun.money.mate.income.navigation.navigateToIncomeList
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.Route
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.spending_plan.navigation.navigateToSpendingPlanAdd
import jun.money.mate.spending_plan.navigation.navigateToSpendingPlanList

class MainNavigator(
    val navController: NavHostController
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Splash

    val currentItem: MainBottomNavItem?
        get() = MainBottomNavItem.find { tab ->
            navController.currentDestination?.hasRoute(tab::class) == true
        }

    fun navigateTo(menuItem: MainBottomNavItem) {
        if (currentItem == menuItem) return

        val navOptions = navOptions {
            popUpTo(0) {
                inclusive = true
            }
            restoreState = true
        }

        when (menuItem) {
            MainBottomNavItem.Home -> navController.navigateToHome(navOptions)
            MainBottomNavItem.SpendingList -> TODO()
            MainBottomNavItem.SavingPlan -> TODO()
            MainBottomNavItem.SpendingPlan -> navController.navigateToSpendingPlanList(navOptions)
        }
    };

    fun navigateToIncomeList() {
        navController.navigateToIncomeList()
    }

    fun navigateToIncomeAdd() {
        navigateToIncomeList()
        navController.navigateToIncomeAdd(AddType.New)
    }

    fun navigateToIncomeEdit(id: Long) {
        navController.navigateToIncomeAdd(AddType.Edit(id))
    }

    fun navigateToSpendingPlanAdd() {
        navController.navigateToSpendingPlanAdd(AddType.New)
    }

    fun navigateToSpendingPlanEdit(id: Long) {
        navController.navigateToSpendingPlanAdd(AddType.Edit(id))
    }

    fun popBackStackIfNotHome(): Boolean {
        return if (isSameCurrentDestination<Route.Splash>() || isSameCurrentDestination<MainTabRoute.Home>()) {
            true
        } else {
            popBackStack()
            false
        }
    }

    private fun popBackStack() {
        navController.popBackStack()
    }

    private inline fun <reified T : Route> isSameCurrentDestination(): Boolean {
        return navController.currentDestination?.hasRoute<T>() == true
    }

    @SuppressLint("RestrictedApi")
    @Composable
    fun shouldShowBottomBar() = MainBottomNavItem.contains {
        currentDestination?.hasRoute(it::class) == true
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
