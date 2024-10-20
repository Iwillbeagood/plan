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
import jun.money.mate.home.navigation.homeNavGraph
import jun.money.mate.income.navigation.incomeNavGraph
import jun.money.mate.main.component.MainBottomNavItem
import jun.money.mate.main.component.MainNavigator
import jun.money.mate.model.etc.error.MessageType
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
            startDestination = navigator.startDestination,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            splashNavGraph(
                onShowHomeScreen = { navigator.navigateTo(MainBottomNavItem.Home) },
            )
            homeNavGraph(
                onShowMenu = {  },
                onShowNotification = {  },
                onShowIncomeList = navigator::navigateToIncomeList,
                onShowIncomeAdd = navigator::navigateToIncomeAdd,
                onShowSpendingList = {  },
                onShowSpendingAdd = {  },
                onShowSnackBar = onShowSnackBar,
            )
            incomeNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                onShowIncomeAdd = navigator::navigateToIncomeAdd,
                onShowSnackBar = onShowSnackBar
            )
        }
    }
}