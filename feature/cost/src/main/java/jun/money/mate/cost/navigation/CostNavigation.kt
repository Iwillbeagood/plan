package jun.money.mate.cost.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.cost.CostAddRoute
import jun.money.mate.cost.CostDetailRoute
import jun.money.mate.cost.CostRoute
import jun.money.mate.navigation.Route

fun NavController.navigateToCost(navOptions: NavOptions) {
    navigate(Route.Cost.Main, navOptions)
}

fun NavController.navigateToCostDetail(id: Long) {
    navigate(Route.Cost.Detail(id))
}

fun NavController.navigateToCostAdd() {
    navigate(Route.Cost.Add)
}

fun NavGraphBuilder.costNavGraph() {
    composable<Route.Cost.Main> {
        CostRoute()
    }
    composable<Route.Cost.Detail> {
        CostDetailRoute()
    }
    composable<Route.Cost.Add> {
        CostAddRoute()
    }
}
