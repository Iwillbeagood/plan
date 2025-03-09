package jun.money.mate.save.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.Route
import jun.money.mate.save.SaveAddRoute
import jun.money.mate.save.SaveDetailRoute
import jun.money.mate.save.SaveListRoute
import java.time.LocalDate

fun NavController.navigateToSaveList(date: LocalDate) {
    navigate(Route.Save.List(date.year, date.monthValue))
}

fun NavController.navigateToSaveDetail(id: Long) {
    navigate(Route.Save.Detail(id))
}

fun NavController.navigateToSaveAdd() {
    navigate(Route.Save.Add)
}

fun NavGraphBuilder.saveNavGraph(
    onGoBack: () -> Unit,
    onShowSavingAdd: () -> Unit,
    onShowSavingDetail: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit
) {
    composable<Route.Save.List> {
        SaveListRoute(
            onGoBack = onGoBack,
            onShowSavingAdd = onShowSavingAdd,
            onShowSavingDetail = onShowSavingDetail,
            onShowSnackBar = onShowSnackBar
        )
    }

    composable<Route.Save.Detail> {
        SaveDetailRoute(
            onGoBack = onGoBack,
            onShowSnackBar = onShowSnackBar
        )
    }

    composable<Route.Save.Add> {
        SaveAddRoute(
            onGoBack = onGoBack,
            onShowSnackBar = onShowSnackBar
        )
    }
}
