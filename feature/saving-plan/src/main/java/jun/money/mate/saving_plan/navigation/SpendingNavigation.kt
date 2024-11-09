package jun.money.mate.saving_plan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute

fun NavController.navigateToSpendingList(navOptions: NavOptions) {
    navigate(MainTabRoute.SpendingList.List, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onShowErrorSnackBar: (MessageType) -> Unit
) {
    composable<MainTabRoute.Home> {
    }
}
