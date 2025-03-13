package jun.money.mate.challenge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jun.money.mate.challenge.SaveAddRoute
import jun.money.mate.challenge.SaveListRoute
import jun.money.mate.navigation.Route
import java.time.YearMonth

fun NavController.navigateToSaveList(date: YearMonth) {
    navigate(Route.Save.List(date.year, date.monthValue))
}

fun NavController.navigateToSaveDetail(id: Long) {
    navigate(Route.Save.Detail(id))
}

fun NavController.navigateToSaveAdd() {
    navigate(Route.Save.Add)
}

fun NavGraphBuilder.saveNavGraph(
) {
    composable<Route.Save.List> {
        SaveListRoute()
    }
    composable<Route.Save.Add> {
        SaveAddRoute()
    }
}
