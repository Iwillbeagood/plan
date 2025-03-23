package jun.money.mate.cost.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jun.money.mate.cost.CostAddRoute
import jun.money.mate.cost.CostRoute
import jun.money.mate.navigation.Route

fun NavController.navigateToCost(id: Long) {
    navigate(Route.Cost.Detail(id))
}

fun NavController.navigateToCostAdd() {
    navigate(Route.Cost.Add)
}

fun NavGraphBuilder.costNavGraph() {
    composable<Route.Cost.Detail> {
        CostRoute()
    }
    composable<Route.Cost.Add> {
        CostAddRoute()
    }
}
