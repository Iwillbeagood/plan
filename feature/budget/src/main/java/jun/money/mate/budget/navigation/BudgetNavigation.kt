package jun.money.mate.budget.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.budget.BudgetAddRoute
import jun.money.mate.budget.BudgetDetailRoute
import jun.money.mate.budget.BudgetRoute
import jun.money.mate.navigation.MainTabRoute

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
        BudgetRoute()
    }
    composable<MainTabRoute.Budget.Add> {
        BudgetAddRoute()
    }
    composable<MainTabRoute.Budget.Detail> {
        BudgetDetailRoute()
    }
}

const val NAV_NAME = "예산"
