package jun.money.mate.income.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import jun.money.mate.income.IncomeAddRoute
import jun.money.mate.income.IncomeListRoute
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.Route
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.navigation.utils.composableType

fun NavController.navigateToIncomeList() {
    navigate(MainTabRoute.Income.List)
}

fun NavController.navigateToIncomeAdd(addType: AddType) {
    navigate(MainTabRoute.Income.Add(addType))
}

fun NavGraphBuilder.incomeNavGraph(
    onGoBack: () -> Unit,
    onShowIncomeAdd: () -> Unit,
    onShowIncomeEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit
) {

    composable<MainTabRoute.Income.List> {
        IncomeListRoute(
            onGoBack = onGoBack,
            onShowIncomeAdd = onShowIncomeAdd,
            onShowIncomeEdit = onShowIncomeEdit,
            onShowSnackBar = onShowSnackBar
        )
    }

    composableType<MainTabRoute.Income.Add, AddType> { backStackEntry ->
        val addType = backStackEntry.toRoute<MainTabRoute.Income.Add>().addType

        IncomeAddRoute(
            addType = addType,
            onGoBack = onGoBack,
            onShowSnackBar = onShowSnackBar
        )
    }
}
