package jun.money.mate.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.Route

fun NavController.navigateToSplash(navOptions: NavOptions) {
    navigate(Route.Splash, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onShowSnackBar: (MessageType) -> Unit
) {
    composable<Route.Splash> {

    }
}
