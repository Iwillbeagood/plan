package jun.money.mate.consumption_spend.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import jun.money.mate.consumption_spend.ConsumptionAddRoute
import jun.money.mate.consumption_spend.ConsumptionListRoute
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.navigation.utils.composableType

fun NavController.navigateToConsumptionList(navOptions: NavOptions) {
    navigate(MainTabRoute.ConsumptionSpend.List, navOptions)
}

fun NavController.navigateToConsumptionAdd(addType: AddType) {
    navigate(MainTabRoute.ConsumptionSpend.Add(addType))
}

fun NavGraphBuilder.consumptionNavGraph(
    onShowSpendingPlanAdd: () -> Unit,
    onShowConsumptionAdd: () -> Unit,
    onShowConsumptionEdit: (id: Long) -> Unit,
) {
    composable<MainTabRoute.ConsumptionSpend.List> {
        ConsumptionListRoute(
            onShowConsumptionAdd = onShowConsumptionAdd,
            onShowSpendingPlanAdd = onShowSpendingPlanAdd,
            onShowConsumptionEdit = onShowConsumptionEdit
        )
    }

    composableType<MainTabRoute.ConsumptionSpend.Add, AddType> { backStackEntry ->
        val addType = backStackEntry.toRoute<MainTabRoute.ConsumptionSpend.Add>().addType

        ConsumptionAddRoute(
            addType = addType
        )
    }
}
