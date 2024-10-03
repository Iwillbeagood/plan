package kic.owner2.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import jun.money.mate.designsystem.component.TwoBtnDialog
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.JUNTheme
import kic.owner2.main.component.MainNavigator
import kic.owner2.main.component.UpdateDialog
import kic.owner2.main.component.rememberMainNavigator
import jun.money.mate.model.etc.error.MessageType
import kic.owner2.stringres.R
import kic.owner2.utils.device.DeviceInfo
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val navigator: MainNavigator = rememberMainNavigator(
                isLoginRequired = viewModel.isLoginRequired()
            )
            val updateDialogEffect by viewModel.mainDialogEffect.collectAsStateWithLifecycle()
            val drawerState = rememberDrawerState(DrawerValue.Closed)

            val coroutineScope = rememberCoroutineScope()
            val snackBarHostState = remember { SnackbarHostState() }
            val onShowErrorSnackBar: (MessageType) -> Unit = { errorType ->
                coroutineScope.launch {
                    when (errorType) {
                        is MessageType.Message -> snackBarHostState.showSnackbar(errorType.message)
                        is MessageType.ResId -> snackBarHostState.showSnackbar(getString(errorType.resId))
                    }
                }
            }

            JunTheme {
                MainScreen(
                    navigator = navigator,
                    drawerState = drawerState,
                    snackBarHostState = snackBarHostState,
                    appRestart = ::appRestart,
                    onStartDial = ::startDial,
                    onStartDifferentApp = { startDifferentApp(it, onShowErrorSnackBar) },
                    onShowErrorSnackBar = onShowErrorSnackBar,
                )

                DialogContent(
                    mainDialogEffect = updateDialogEffect,
                    onDismissRequest = viewModel::onDismissUpdateDialog,
                    onFinishApp = ::finish,
                    onStartAppStoreToUpdate = { startDifferentApp(it, onShowErrorSnackBar) }
                )

                MainBackHandler(
                    navigator = navigator,
                    drawerState = drawerState,
                    onShowAppCloseDialog = viewModel::onShowAppCloseDialog
                )

                LaunchedEffect(true) {
                    viewModel.initViewModel(DeviceInfo.getAppVersionName(this@MainActivity))
                }
            }
        }
    }

    private fun appRestart() {
        val packageManager = packageManager
        val intent = packageManager?.getLaunchIntentForPackage(packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        startActivity(mainIntent)
        exitProcess(0)
    }
}

private fun Context.startDifferentApp(url: String, onError: (MessageType) -> Unit = {}) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        onError(MessageType.ResId( kic.owner2.main.R.string.error_message_url))
    }
}

private fun Context.startDial(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
    startActivity(intent)
}

@Composable
private fun DialogContent(
    mainDialogEffect: MainDialogEffect,
    onDismissRequest: () -> Unit,
    onFinishApp: () -> Unit,
    onStartAppStoreToUpdate: (String) -> Unit
) {

    when (mainDialogEffect) {
        MainDialogEffect.Idle -> {}
        is MainDialogEffect.UpdateOptional -> {
            UpdateDialog(
                message = mainDialogEffect.message,
                onDismissRequest = onDismissRequest,
                onNegativeClick = onDismissRequest,
                onPositiveClick = {
                    onStartAppStoreToUpdate(mainDialogEffect.url)
                    onFinishApp()
                }
            )
        }

        is MainDialogEffect.UpdateForced -> {
            UpdateDialog(
                message = mainDialogEffect.message,
                button1Text = stringResource(id = R.string.btn_exit),
                onDismissRequest = onDismissRequest,
                onNegativeClick = {
                    onDismissRequest()
                    onFinishApp()
                },
                onPositiveClick = {
                    onStartAppStoreToUpdate(mainDialogEffect.url)
                    onFinishApp()
                }
            )
        }
        MainDialogEffect.AppClose -> {
            TwoBtnDialog(
                title = stringResource(id = kic.owner2.main.R.string.app_close_dialog_title),
                onDismissRequest = onDismissRequest,
                button1Click = onDismissRequest,
                button2Text = stringResource(id = R.string.btn_yes),
                button2Click = onFinishApp,
                content = {
                    Text(
                        text = stringResource(id = kic.owner2.main.R.string.app_close_dialog_content),
                        style = JUNTheme.typography.titleMediumR
                    )
                }
            )
        }
    }
}

@Composable
private fun MainBackHandler(
    navigator: MainNavigator,
    drawerState: DrawerState,
    onShowAppCloseDialog: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        if (drawerState.isOpen) {
            coroutineScope.launch {
                drawerState.close()
            }
        } else {
            val isHome = navigator.popBackStackIfNotHome()
            if (isHome) {
                onShowAppCloseDialog()
            }
        }
    }
}