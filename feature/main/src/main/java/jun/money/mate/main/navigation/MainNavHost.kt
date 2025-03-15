package jun.money.mate.main.navigation

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
import jun.money.mate.challenge.navigation.challengeNavGraph
import jun.money.mate.consumption_spend.navigation.consumptionNavGraph
import jun.money.mate.finance.navigation.financeNavGraph
import jun.money.mate.home.navigation.homeNavGraph
import jun.money.mate.income.navigation.incomeNavGraph
import jun.money.mate.navigation.MainBottomNavItem
import jun.money.mate.navigation.Route
import jun.money.mate.save.navigation.saveNavGraph
import jun.money.mate.spending_plan.navigation.spendingPlanNavGraph
import jun.money.mate.splash.navigation.splashNavGraph

@Composable
internal fun MainNavHost(
    paddingValues: PaddingValues,
    navigator: MainNavigator,
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
            homeNavGraph()
            financeNavGraph()
            incomeNavGraph()
            challengeNavGraph()
            spendingPlanNavGraph(
                onShowSpendingPlanAdd = navigator::navigateToSpendingPlanAdd,
                onShowSpendingPlanEdit = navigator::navigateToSpendingPlanEdit,
            )
            consumptionNavGraph(
                onShowConsumptionAdd = navigator::navigateToConsumptionAdd,
                onShowSpendingPlanAdd = navigator::navigateToSpendingPlanAdd,
                onShowConsumptionEdit = navigator::navigateToConsumptionEdit,
            )
            saveNavGraph()
            splashNavGraph(
                onShowHomeScreen = {
                    navigator.navigateTo(MainBottomNavItem.Home)
                }
            )
        }
    }
}