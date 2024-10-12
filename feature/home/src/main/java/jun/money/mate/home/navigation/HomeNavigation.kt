package jun.money.mate.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.home.HomeRoute
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(MainTabRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onShowMenu: () -> Unit,
    onShowNotification: () -> Unit,
    onShowIncomeList: () -> Unit,
    onShowIncomeAdd: () -> Unit,
    onShowSpendingList: () -> Unit,
    onShowSpendingAdd: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit
) {
    composable<MainTabRoute.Home> {
        HomeRoute(
            onShowMenu = onShowMenu,
            onShowNotification = onShowNotification,
            onShowSnackBar = onShowSnackBar,
            onShowIncomeList = onShowIncomeList,
            onShowIncomeAdd = onShowIncomeAdd,
            onShowSpendingList = onShowSpendingList,
            onShowSpendingAdd = onShowSpendingAdd
        )
    }
}
