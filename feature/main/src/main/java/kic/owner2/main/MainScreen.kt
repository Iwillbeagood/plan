package kic.owner2.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kic.owner2.main.component.MainNavigator
import kic.owner2.main.component.MenuDrawerSheet
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.etc.ConnectionState
import jun.money.mate.network.connectivityState
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
    drawerState: DrawerState,
    appRestart: () -> Unit,
    onStartDial: (String) -> Unit,
    onStartDifferentApp: (String) -> Unit,
    onShowErrorSnackBar: (MessageType) -> Unit,
) {
    val connection by connectivityState()

    val coroutineScope = rememberCoroutineScope()

    MainScreenContent(
        navigator = navigator,
        connection = connection,
        snackBarHostState = snackBarHostState,
        drawerState = drawerState,
        appRestart = appRestart,
        onShowMenu = {
            coroutineScope.launch {
                drawerState.open()
            }
        },
        onDismissMenu = {
            coroutineScope.launch {
                drawerState.close()
            }
        },
        onStartDial = onStartDial,
        onStartDifferentApp = onStartDifferentApp,
        onShowErrorSnackBar = onShowErrorSnackBar,
    )
}



@Composable
private fun MainScreenContent(
    navigator: MainNavigator,
    connection: ConnectionState,
    drawerState: DrawerState,
    snackBarHostState: SnackbarHostState,
    appRestart: () -> Unit,
    onShowMenu: () -> Unit,
    onDismissMenu: () -> Unit,
    onStartDial: (String) -> Unit,
    onStartDifferentApp: (String) -> Unit,
    onShowErrorSnackBar: (MessageType) -> Unit,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuDrawerSheet(
                onDismiss = onDismissMenu,
                navigateTo = navigator::navigateTo,
            )
        },
        gesturesEnabled = false
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            topBar = {
                if (connection != ConnectionState.Available) {
                    Text(
                        text = "network disconnected", modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Red)
                            .padding(vertical = 20.dp)
                    )
                }
            },
            content = {
                MainNavHost(
                    navigator = navigator,
                    paddingValues = it,
                    appRestart = appRestart,
                    onShowErrorSnackBar = onShowErrorSnackBar,
                    onStartDial = onStartDial,
                    onStartDifferentApp = onStartDifferentApp,
                    onShowMenu = onShowMenu
                )
            },
            snackbarHost = { SnackbarHost(snackBarHostState) }
        )
    }
}
