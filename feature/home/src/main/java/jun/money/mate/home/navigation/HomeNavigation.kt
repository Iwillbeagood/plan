package jun.money.mate.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import jun.money.mate.model.error.MessageType
import jun.money.mate.navigation.Route

fun NavController.navigateToHome() {
    val navOptions = navOptions {
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
    }

    navigate(Route.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onShowErrorSnackBar: (MessageType) -> Unit
) {
    composable<Route.Home> {
    }
}
