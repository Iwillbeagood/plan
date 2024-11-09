package jun.money.mate.spending_list.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute

fun NavController.navigateToSpending(navOptions: NavOptions) {
    navigate(MainTabRoute.SpendingList.List, navOptions)
}

fun NavGraphBuilder.spendingNavGraph(
    onShowErrorSnackBar: (MessageType) -> Unit
) {
    composable<MainTabRoute.SpendingList.List> {

    }
}
