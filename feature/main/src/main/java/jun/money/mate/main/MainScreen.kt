package jun.money.mate.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import jun.money.mate.main.component.MainBottomBar
import jun.money.mate.main.component.MainNavigator
import jun.money.mate.model.etc.ConnectionState
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainBottomNavItem
import jun.money.mate.network.connectivityState
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainScreen(
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
    appRestart: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
) {
    val connection by connectivityState()

    MainScreenContent(
        navigator = navigator,
        connection = connection,
        snackBarHostState = snackBarHostState,
        appRestart = appRestart,
        onShowSnackBar = onShowSnackBar,
    )
}

@Composable
private fun MainScreenContent(
    navigator: MainNavigator,
    connection: ConnectionState,
    snackBarHostState: SnackbarHostState,
    appRestart: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        bottomBar = {
            MainBottomBar(
                visible = navigator.shouldShowBottomBar(),
                bottomItems = MainBottomNavItem.entries.toPersistentList(),
                currentItem = navigator.currentItem,
                onBottomItemClicked = navigator::navigateTo
            )
        },
        content = {
            MainNavHost(
                navigator = navigator,
                paddingValues = it,
                appRestart = appRestart,
                onShowSnackBar = onShowSnackBar,
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.imePadding()
            )
        }
    )
}
