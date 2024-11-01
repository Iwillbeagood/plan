package jun.money.mate.spending_plan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.navigation.utils.composableType
import jun.money.mate.spending_plan.SpendingListScreen

fun NavController.navigateToSpendingPlanList(navOptions: NavOptions) {
    navigate(MainTabRoute.SpendingPlan.List, navOptions)
}

fun NavController.navigateToSpendingPlanAdd(addType: AddType) {
    navigate(MainTabRoute.SpendingPlan.Add(addType))
}

fun NavGraphBuilder.spendingPlanNavGraph(
    onGoBack: () -> Unit,
    onShowErrorSnackBar: (MessageType) -> Unit,
) {
    composable<MainTabRoute.SpendingPlan.List> {
        SpendingListScreen(
            onGoBack = onGoBack,
            onShowSpendingPlanAdd = { /* TODO */ },
            onShowSpendingPlanEdit = { /* TODO */ },
            onShowSnackBar = onShowErrorSnackBar
        )
    }

    composableType<MainTabRoute.SpendingPlan.Add, AddType>() {
        // TODO
    }
}
