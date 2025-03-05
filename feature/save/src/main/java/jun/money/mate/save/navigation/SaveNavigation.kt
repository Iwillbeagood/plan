package jun.money.mate.save.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.Route
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.navigation.utils.composableType
import jun.money.mate.save.SaveAddRoute
import jun.money.mate.save.SaveListRoute

fun NavController.navigateToSaveList() {
    navigate(Route.Save.List)
}

fun NavController.navigateToSaveAdd() {
    navigate(Route.Save.Add)
}

fun NavController.navigateToSaveEdit(id: Long) {
    navigate(Route.Save.Edit(id))
}

fun NavGraphBuilder.saveNavGraph(
    onGoBack: () -> Unit,
    onShowSavingAdd: () -> Unit,
    onShowSavingEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit
) {
    composable<Route.Save.List> {
        SaveListRoute(
            onShowSavingAdd = onShowSavingAdd,
            onShowSavingEdit = onShowSavingEdit,
            onShowSnackBar = onShowSnackBar
        )
    }

    composable<Route.Save.Add> {
        SaveAddRoute(
            onGoBack = onGoBack,
            onShowSnackBar = onShowSnackBar
        )
    }

    composable<Route.Save.Edit> {

    }
}
