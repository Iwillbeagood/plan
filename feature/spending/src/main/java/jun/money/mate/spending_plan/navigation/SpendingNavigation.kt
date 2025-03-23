package jun.money.mate.spending_plan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.spending_plan.SpendingRoute

fun NavController.navigateToSpending(navOptions: NavOptions) {
    navigate(MainTabRoute.Spending, navOptions)
}

fun NavGraphBuilder.spendingNavGraph() {
    composable<MainTabRoute.Spending> {
        SpendingRoute()
    }
}
