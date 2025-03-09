package jun.money.mate.main

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import jun.money.mate.consumption_spend.navigation.consumptionNavGraph
import jun.money.mate.finance.navigation.financeNavGraph
import jun.money.mate.home.navigation.homeNavGraph
import jun.money.mate.income.navigation.incomeNavGraph
import jun.money.mate.main.component.MainNavigator
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainBottomNavItem
import jun.money.mate.navigation.Route
import jun.money.mate.save.navigation.saveNavGraph
import jun.money.mate.spending_plan.navigation.spendingPlanNavGraph
import jun.money.mate.splash.navigation.splashNavGraph

@Composable
internal fun MainNavHost(
    paddingValues: PaddingValues,
    navigator: MainNavigator,
    appRestart: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(paddingValues)
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = Route.Splash,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            homeNavGraph(
                onShowMenu = { },
                onShowNotification = { },
                onShowMainNavScreen = navigator::navigateTo,
                onShowIncomeAdd = navigator::navigateToIncomeAdd,
                onShowSpendingAdd = navigator::navigateToSpendingPlanAdd,
                onShowSaveAdd = navigator::navigateToSavingAdd,
                onShowConsumptionAdd = navigator::navigateToConsumptionAdd,
                onShowSnackBar = onShowSnackBar,
            )
            financeNavGraph(
                onShowIncome = navigator::navigateToIncomeList,
                onShowSaving = navigator::navigateToSaveList,
                onShowSnackBar = onShowSnackBar
            )
            incomeNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                onShowIncomeAdd = navigator::navigateToIncomeAdd,
                onShowIncomeEdit = navigator::navigateToIncomeEdit,
                onShowSnackBar = onShowSnackBar
            )
            spendingPlanNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                onShowSpendingPlanAdd = navigator::navigateToSpendingPlanAdd,
                onShowSpendingPlanEdit = navigator::navigateToSpendingPlanEdit,
                onShowErrorSnackBar = onShowSnackBar
            )
            consumptionNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                onShowConsumptionAdd = navigator::navigateToConsumptionAdd,
                onShowSpendingPlanAdd = navigator::navigateToSpendingPlanAdd,
                onShowConsumptionEdit = navigator::navigateToConsumptionEdit,
                onShowSnackBar = onShowSnackBar
            )
            saveNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                onShowSavingAdd = navigator::navigateToSavingAdd,
                onShowSavingDetail = navigator::navigateToSavingDetail,
                onShowSnackBar = onShowSnackBar
            )
            splashNavGraph(
                onShowHomeScreen = {
                    navigator.navigateTo(MainBottomNavItem.Home)
                }
            )
        }
    }
}