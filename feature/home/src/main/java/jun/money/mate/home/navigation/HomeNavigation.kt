package jun.money.mate.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.home.HomeRoute
import jun.money.mate.navigation.MainTabRoute

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(MainTabRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph() {
    composable<MainTabRoute.Home> {
        HomeRoute()
    }
}
