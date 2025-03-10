package jun.money.mate.spending_plan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.navigation.utils.composableType
import jun.money.mate.spending_plan.SpendingPlanAddRoute
import jun.money.mate.spending_plan.SpendingPlanListRoute

fun NavController.navigateToSpendingPlanList(navOptions: NavOptions) {
    navigate(MainTabRoute.SpendingPlan.List, navOptions)
}

fun NavController.navigateToSpendingPlanAdd(addType: AddType) {
    navigate(MainTabRoute.SpendingPlan.Add(addType))
}

fun NavGraphBuilder.spendingPlanNavGraph(
    onShowSpendingPlanAdd: () -> Unit,
    onShowSpendingPlanEdit: (id: Long) -> Unit,
) {
    composable<MainTabRoute.SpendingPlan.List> {
        SpendingPlanListRoute(
            onShowSpendingPlanAdd = onShowSpendingPlanAdd,
            onShowSpendingPlanEdit = onShowSpendingPlanEdit,
        )
    }

    composableType<MainTabRoute.SpendingPlan.Add, AddType> { backStackEntry ->
        val addType = backStackEntry.toRoute<MainTabRoute.SpendingPlan.Add>().addType

        SpendingPlanAddRoute(
            addType = addType
        )
    }
}
