package jun.money.mate.splash

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.ui.SplashScreen
import kotlinx.coroutines.delay

@Composable
internal fun SplashRoute(
    onShowHomeScreen: () -> Unit,
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.primary)

    SplashScreen()

    LaunchedEffect(Unit) {
        delay(1500)
        onShowHomeScreen()
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    JunTheme {
        SplashScreen()
    }
}
