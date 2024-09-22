package kic.owner2.main

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
import kic.owner2.settlement.navigation.settlementNavGraph
import kic.owner2.cargoregistration.navigation.cargoRegistrationsNavGraph
import jun.money.mate.home.navigation.homeNavGraph
import kic.owner2.login.navigation.loginNavGraph
import kic.owner2.customer_service.navigation.customerServiceNavGraph
import kic.owner2.main.component.MainMenuNavItem
import kic.owner2.main.component.MainNavigator
import jun.money.mate.model.error.MessageType
import kic.owner2.myInfo.navigation.myInfoNavGraph
import kic.owner2.notice.navigation.noticeNavGraph
import kic.owner2.push.navigation.pushNavGraph
import kic.owner2.setting.navigation.settingNavGraph
import kic.owner2.splash.navigation.splashNavGraph
import jun.money.mate.utils.Constants

@Composable
internal fun MainNavHost(
    paddingValues: PaddingValues,
    navigator: MainNavigator,
    appRestart: () -> Unit,
    onShowErrorSnackBar: (MessageType) -> Unit,
    onStartDial: (String) -> Unit,
    onStartDifferentApp: (String) -> Unit,
    onShowMenu: () -> Unit,
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
                onPermissionAllowed = navigator::navigateToLoginOrHome,
                onShowErrorSnackBar = onShowErrorSnackBar
            )
            loginNavGraph(
                onLoginSuccess = navigator::navigateToHome,
                onShowErrorSnackBar = onShowErrorSnackBar
            )
            cargoRegistrationsNavGraph(
                onShowErrorSnackBar = onShowErrorSnackBar,
                onBackEvent = navigator::popBackStackIfNotHome
            )
            homeNavGraph(
                onShowMenu = onShowMenu,
                onStartDial = onStartDial,
                onShowCargoAddScreen = { navigator.navigateToCargoRegistration() },
                onShowSharedCargoScreen = navigator::navigateToSharedCargoScreen,
                onShowDepositScreen = { navigator.navigateTo(MainMenuNavItem.Deposit) },
                onShowMyInfoScreen = navigator::navigateToMyInfoScreen,
                onShowMileageScreen = { navigator.navigateTo(MainMenuNavItem.Mileage) },
                onShowMyCargoListScreen = navigator::navigateToMyCargoListScreen,
                onShowPushScreen = navigator::navigateToPushScreen,
                onShowSettingScreen = { navigator.navigateTo(MainMenuNavItem.Setting) },
                onShowDispatchedCargoScreen = navigator::navigateToDispatchedCargoScreen,
                onShowErrorSnackBar = onShowErrorSnackBar,
            )
            customerServiceNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                onCallCustomerService = { onStartDial(Constants.CALL_CENTER_NUMBER) },
                onShowErrorSnackBar = onShowErrorSnackBar
            )
            settingNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                appRestart = appRestart,
                onShowMyInfoScreen = {},
                onShowPushSetting = navigator::navigateToPushSetting,
                onStartAppStoreToUpdate = onStartDifferentApp
            )
            pushNavGraph(
                onGoBack = navigator::popBackStackIfNotHome
            )
            noticeNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                onShowErrorSnackBar = onShowErrorSnackBar
            )
            settlementNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                onShowErrorSnackBar = onShowErrorSnackBar
            )
            myInfoNavGraph(
                onGoBack = navigator::popBackStackIfNotHome,
                onShowErrorSnackBar = onShowErrorSnackBar
            )
        }
    }
}