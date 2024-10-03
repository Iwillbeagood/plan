package jun.money.mate.splash

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem.theme.nonScaledSp
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.stringres.R

@Composable
internal fun SplashRoute(
    onShowSnackbar: (MessageType) -> Unit,
) {

}

@Composable
private fun SplashScreen(
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                color = White1,
                style = JUNTheme.typography.titleLargeR.nonScaledSp,
            )
            Text(
                text = stringResource(id = R.string.app_name),
                color = White1,
                style = JUNTheme.typography.titleLargeR.copy(
                    fontSize = 80.sp,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ).nonScaledSp
            )
        }
        Text(
            text = stringResource(id = R.string.app_copyright),
            color = White1,
            style = JUNTheme.typography.titleSmallR.nonScaledSp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    JunTheme {
        SplashScreen()
    }
}