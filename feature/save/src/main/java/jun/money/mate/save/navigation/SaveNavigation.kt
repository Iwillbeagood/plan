package jun.money.mate.save.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jun.money.mate.navigation.Route
import jun.money.mate.save.SaveAddRoute
import jun.money.mate.save.SaveDetailRoute
import jun.money.mate.save.SaveListRoute

fun NavController.navigateToSaveList() {
    navigate(Route.Save.List)
}

fun NavController.navigateToSaveDetail(id: Long) {
    navigate(Route.Save.Detail(id))
}

fun NavController.navigateToSaveAdd() {
    navigate(Route.Save.Add)
}

fun NavGraphBuilder.saveNavGraph() {
    composable<Route.Save.List> {
        SaveListRoute()
    }
    composable<Route.Save.Detail> {
        SaveDetailRoute()
    }
    composable<Route.Save.Add> {
        SaveAddRoute()
    }
}
