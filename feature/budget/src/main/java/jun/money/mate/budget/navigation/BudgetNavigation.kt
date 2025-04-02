package jun.money.mate.budget.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import jun.money.mate.budget.BudgetRoute
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.navigation.utils.composableType

fun NavController.navigateToBudget(navOptions: NavOptions) {
    navigate(MainTabRoute.Budget.Main, navOptions)
}

fun NavController.navigateToBudgetDetail(id: Long) {
    navigate(MainTabRoute.Budget.Detail(id))
}

fun NavController.navigateToBudgetAdd() {
    navigate(MainTabRoute.Budget.Add)
}

fun NavGraphBuilder.budgetNavGraph() {
    composable<MainTabRoute.Budget.Main> {
        BudgetRoute(
        )
    }
    composable<MainTabRoute.Budget.Main> {

    }
    composable<MainTabRoute.Budget.Add> {

    }
}
