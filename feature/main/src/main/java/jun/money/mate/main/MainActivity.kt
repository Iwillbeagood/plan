package jun.money.mate.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import jun.money.mate.designsystem.component.TwoBtnDialog
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.main.component.MainNavigator
import jun.money.mate.main.component.rememberMainNavigator
import jun.money.mate.model.etc.error.MessageType
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val navigator: MainNavigator = rememberMainNavigator()
            val mainDialogEffect by viewModel.mainDialogEffect.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()
            val snackBarHostState = remember { SnackbarHostState() }
            val onShowSnackBar: (MessageType) -> Unit = { messageType ->
                coroutineScope.launch {
                    when (messageType) {
                        is MessageType.Message -> snackBarHostState.showSnackbar(messageType.message)
                        is MessageType.ResId -> snackBarHostState.showSnackbar(getString(messageType.resId))
                    }
                }
            }

            JunTheme {
                MainScreen(
                    navigator = navigator,
                    snackBarHostState = snackBarHostState,
                    appRestart = ::appRestart,
                    onShowSnackBar = onShowSnackBar,
                )

                DialogContent(
                    mainDialogEffect = mainDialogEffect,
                    onDismissRequest = viewModel::onDialogDismiss,
                    onFinishApp = ::finish,
                )

                MainBackHandler(
                    navigator = navigator,
                    onShowAppCloseDialog = viewModel::onShowAppCloseDialog
                )
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

@Composable
private fun DialogContent(
    mainDialogEffect: MainDialogEffect,
    onDismissRequest: () -> Unit,
    onFinishApp: () -> Unit,
) {

    when (mainDialogEffect) {
        MainDialogEffect.Idle -> {}
        MainDialogEffect.AppClose -> {
            TwoBtnDialog(
                title = stringResource(id = R.string.app_close_dialog_title),
                onDismissRequest = onDismissRequest,
                button1Click = onDismissRequest,
                button2Text = stringResource(id = jun.money.mate.res.R.string.btn_yes),
                button2Click = onFinishApp,
                content = {
                    Text(
                        text = stringResource(id = R.string.app_close_dialog_content),
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
    onShowAppCloseDialog: () -> Unit
) {
    BackHandler {
        val isHome = navigator.popBackStackIfNotHome()
        if (isHome) {
            onShowAppCloseDialog()
        }
    }
}