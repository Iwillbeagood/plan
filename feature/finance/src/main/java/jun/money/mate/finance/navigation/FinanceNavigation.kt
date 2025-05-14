package jun.money.mate.finance.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.finance.FinanceRoute
import jun.money.mate.navigation.MainTabRoute

fun NavController.navigateToFinance(navOptions: NavOptions) {
    navigate(MainTabRoute.Finance, navOptions)
}

fun NavGraphBuilder.financeNavGraph() {
    composable<MainTabRoute.Finance> {
        FinanceRoute()
    }
}
