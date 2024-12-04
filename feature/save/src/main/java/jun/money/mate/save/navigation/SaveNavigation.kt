package jun.money.mate.save.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.navigation.utils.composableType
import jun.money.mate.save.SaveAddRoute
import jun.money.mate.save.SaveListRoute

fun NavController.navigateToSaveList(navOptions: NavOptions) {
    navigate(MainTabRoute.Save.List, navOptions)
}

fun NavController.navigateToSaveAdd(addType: AddType) {
    navigate(MainTabRoute.Save.Add(addType))
}

fun NavGraphBuilder.saveNavGraph(
    onGoBack: () -> Unit,
    onShowSavingAdd: () -> Unit,
    onShowSavingEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit
) {
    composable<MainTabRoute.Save.List> {
        SaveListRoute(
            onShowSavingAdd = onShowSavingAdd,
            onShowSavingEdit = onShowSavingEdit,
            onShowSnackBar = onShowSnackBar
        )
    }

    composableType<MainTabRoute.Save.Add, AddType> { backStackEntry ->
        val addType = backStackEntry.toRoute<MainTabRoute.ConsumptionSpend.Add>().addType

        SaveAddRoute(
            addType = addType,
            onGoBack = onGoBack,
            onShowSnackBar = onShowSnackBar
        )
    }
}
