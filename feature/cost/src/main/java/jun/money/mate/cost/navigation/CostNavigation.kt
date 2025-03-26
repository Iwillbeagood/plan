package jun.money.mate.cost.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.cost.CostAddRoute
import jun.money.mate.cost.CostDetailRoute
import jun.money.mate.cost.CostRoute
import jun.money.mate.navigation.MainTabRoute

fun NavController.navigateToCost(navOptions: NavOptions) {
    navigate(MainTabRoute.Cost.Main, navOptions)
}

fun NavController.navigateToCostDetail(id: Long) {
    navigate(MainTabRoute.Cost.Detail(id))
}

fun NavController.navigateToCostAdd() {
    navigate(MainTabRoute.Cost.Add)
}

fun NavGraphBuilder.costNavGraph() {
    composable<MainTabRoute.Cost.Main> {
        CostRoute()
    }
    composable<MainTabRoute.Cost.Detail> {
        CostDetailRoute()
    }
    composable<MainTabRoute.Cost.Add> {
        CostAddRoute()
    }
}
