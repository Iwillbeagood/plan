package jun.money.mate.main.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import jun.money.mate.challenge.navigation.navigateToChallengeAdd
import jun.money.mate.challenge.navigation.navigateToChallengeDetail
import jun.money.mate.consumption_spend.navigation.navigateToConsumptionAdd
import jun.money.mate.cost.navigation.navigateToCost
import jun.money.mate.cost.navigation.navigateToCostDetail
import jun.money.mate.cost.navigation.navigateToCostAdd
import jun.money.mate.finance.navigation.navigateToFinance
import jun.money.mate.home.navigation.navigateToHome
import jun.money.mate.income.navigation.navigateToIncomeAdd
import jun.money.mate.income.navigation.navigateToIncomeEdit
import jun.money.mate.income.navigation.navigateToIncomeList
import jun.money.mate.navigation.MainBottomNavItem
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.Route
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.save.navigation.navigateToSaveAdd
import jun.money.mate.save.navigation.navigateToSaveDetail
import jun.money.mate.save.navigation.navigateToSaveList
import jun.money.mate.navigation.interop.NavigateActionInterop

class MainNavigator(
    val navController: NavHostController
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentItem: MainBottomNavItem?
        get() = MainBottomNavItem.find { tab ->
            navController.currentDestination?.hasRoute(tab::class) == true
        }

    fun navigationInteropImpl(): NavigateActionInterop {
        return object : NavigateActionInterop {
            override fun popBackStack() {
                popBackStackIfNotHome()
            }

            override fun navigateBottomNav(item: MainBottomNavItem) {
                if (currentItem == item) return

                val navOptions = navOptions {
                    popUpTo(0) {
                        inclusive = true
                    }
                    restoreState = true
                }

                when (item) {
                    MainBottomNavItem.Home -> navController.navigateToHome(navOptions)
                    MainBottomNavItem.Calendar -> TODO()
                    MainBottomNavItem.Finance -> navController.navigateToFinance(navOptions)
                    MainBottomNavItem.Cost -> navController.navigateToCost(navOptions)
                }
            }

            override fun navigateToIncomeList() = navController.navigateToIncomeList()
            override fun navigateToSaveList() = navController.navigateToSaveList()
            override fun navigateToIncomeAdd() = navController.navigateToIncomeAdd()
            override fun navigateToIncomeEdit(id: Long) = navController.navigateToIncomeEdit(id)
            override fun navigateToConsumptionAdd() = navController.navigateToConsumptionAdd(AddType.New)
            override fun navigateToConsumptionEdit(id: Long) = navController.navigateToConsumptionAdd(AddType.Edit(id))
            override fun navigateToSavingAdd() = navController.navigateToSaveAdd()
            override fun navigateToSavingDetail(id: Long) = navController.navigateToSaveDetail(id)
            override fun navigateToChallengeAdd() = navController.navigateToChallengeAdd()
            override fun navigateToChallengeDetail(id: Long) = navController.navigateToChallengeDetail(id)
            override fun navigateToCostAdd() = navController.navigateToCostAdd()
            override fun navigateToCostDetail(id: Long) = navController.navigateToCostDetail(id)
        }
    }

    fun navigateToConsumptionAdd() {
        navController.navigateToConsumptionAdd(AddType.New)
    }

    fun navigateToConsumptionEdit(id: Long) {
        navController.navigateToConsumptionAdd(AddType.Edit(id))
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
