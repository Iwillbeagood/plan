package kic.owner2.main.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kic.owner2.cargoregistration.navigation.navigateToCargoRegistration
import kic.owner2.settlement.navigation.navigateToSettlement
import kic.owner2.customer_service.navigation.navigateToCustomerService
import kic.owner2.deposit.navigation.navigateToDeposit
import jun.money.mate.home.navigation.navigateToHome
import kic.owner2.login.navigation.navigateToLogin
import kic.owner2.model.cargo.CargoStatus
import kic.owner2.myInfo.navigation.navigateToMyInfo
import jun.money.mate.navigation.Route
import kic.owner2.notice.navigation.navigateToNotice
import kic.owner2.push.navigation.navigateToPush
import kic.owner2.setting.navigation.navigateToPushSetting
import kic.owner2.setting.navigation.navigateToSetting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainNavigator(
    val navController: NavHostController,
    private val isLoginRequired: Boolean
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Splash

    val currentItem: MainMenuNavItem?
        @Composable get() = MainMenuNavItem.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    fun navigateTo(menuItem: MainMenuNavItem) {
        when (menuItem) {
            MainMenuNavItem.CustomerService -> navController.navigateToCustomerService()
            MainMenuNavItem.Mileage -> {}
            MainMenuNavItem.Deposit -> navController.navigateToDeposit()
            MainMenuNavItem.Settlement -> navController.navigateToSettlement()
            MainMenuNavItem.Notice -> navController.navigateToNotice()
            MainMenuNavItem.Setting -> navController.navigateToSetting()
            MainMenuNavItem.MyList -> {}
        }
    }

    fun navigateToLoginOrHome() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            if (isLoginRequired) {
                navigateToLogin()
            } else {
                navigateToHome()
            }
        }
    }

    fun navigateToLogin() {
        navController.navigateToLogin()
    }

    fun navigateToHome() {
        navController.navigateToHome()
    }

    fun navigateToSharedCargoScreen() {

    }

    fun navigateToMyInfoScreen() {
        navController.navigateToMyInfo()
    }

    fun navigateToMyCargoListScreen(cargoStatus: CargoStatus) {

    }

    fun navigateToPushScreen() {
        navController.navigateToPush()
    }

    fun navigateToDispatchedCargoScreen(orderNumber: String) {

    }

    fun navigateToPushSetting() {
        navController.navigateToPushSetting()
    }

    fun navigateToCargoRegistration(

    ) {
        navController.navigateToCargoRegistration()
    }

    fun popBackStackIfNotHome(): Boolean {
        return if (isSameCurrentDestination<Route.Splash>() || isSameCurrentDestination<Route.Login>() || isSameCurrentDestination<Route.Home>()) {
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
}

@Composable
fun rememberMainNavigator(
    isLoginRequired: Boolean,
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController, isLoginRequired)
}
