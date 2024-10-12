package jun.money.mate.income.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.income.IncomeAddRoute
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.Route

fun NavController.navigateToIncomeList() {
    navigate(Route.Income.List)
}

fun NavController.navigateToIncomeAdd() {
    navigate(Route.Income.Add)
}

fun NavGraphBuilder.incomeNavGraph(
    onBackClick: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit
) {

    composable<Route.Income.List> {

    }

    composable<Route.Income.Add> {
        IncomeAddRoute(
            onBackClick = onBackClick,
            onShowSnackBar = onShowSnackBar
        )
    }
}
