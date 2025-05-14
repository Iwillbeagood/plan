package jun.money.mate.income.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jun.money.mate.income.IncomeAddRoute
import jun.money.mate.income.IncomeEditRoute
import jun.money.mate.income.IncomeListRoute
import jun.money.mate.navigation.Route

fun NavController.navigateToIncomeList() {
    navigate(Route.Income.List)
}

fun NavController.navigateToIncomeAdd() {
    navigate(Route.Income.Add)
}

fun NavController.navigateToIncomeEdit(id: Long) {
    navigate(Route.Income.Edit(id))
}

fun NavGraphBuilder.incomeNavGraph() {
    composable<Route.Income.List> {
        IncomeListRoute()
    }
    composable<Route.Income.Add> {
        IncomeAddRoute()
    }
    composable<Route.Income.Edit> {
        IncomeEditRoute()
    }
}
