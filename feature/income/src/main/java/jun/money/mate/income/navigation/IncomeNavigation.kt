package jun.money.mate.income.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.income.IncomeAddRoute
import jun.money.mate.income.IncomeEditRoute
import jun.money.mate.income.IncomeListRoute
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute

fun NavController.navigateToIncomeList() {
    navigate(MainTabRoute.Income.List)
}

fun NavController.navigateToIncomeAdd() {
    navigate(MainTabRoute.Income.Add)
}

fun NavController.navigateToIncomeEdit(id: Long) {
    navigate(MainTabRoute.Income.Edit(id))
}

fun NavGraphBuilder.incomeNavGraph(
    onGoBack: () -> Unit,
    onShowIncomeAdd: () -> Unit,
    onShowIncomeEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit
) {

    composable<MainTabRoute.Income.List> {
        IncomeListRoute(
            onShowIncomeAdd = onShowIncomeAdd,
            onShowIncomeEdit = onShowIncomeEdit,
            onShowSnackBar = onShowSnackBar
        )
    }

    composable<MainTabRoute.Income.Add> { backStackEntry ->
        IncomeAddRoute(
            onGoBack = onGoBack,
            onShowSnackBar = onShowSnackBar
        )
    }

    composable<MainTabRoute.Income.Edit> { backStackEntry ->
        IncomeEditRoute(
            onGoBack = onGoBack,
            onShowSnackBar = onShowSnackBar
        )
    }
}
