package jun.money.mate.finance.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.finance.FinanceRoute
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute
import java.time.LocalDate

fun NavController.navigateToFinance(navOptions: NavOptions) {
    navigate(MainTabRoute.Finance, navOptions)
}

fun NavGraphBuilder.financeNavGraph(
    onShowIncome: (LocalDate) -> Unit,
    onShowSaving: (LocalDate) -> Unit,
    onShowSnackBar: (MessageType) -> Unit
) {
    composable<MainTabRoute.Finance> {
        FinanceRoute(
            onShowIncome = onShowIncome,
            onShowSavings = onShowSaving,
            onShowSnackBar = onShowSnackBar,
        )
    }
}
