package jun.money.mate.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem.theme.nonScaledSp
import jun.money.mate.res.R

@Composable
fun SplashScreen() {
    var visible by remember { mutableStateOf(false) }

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
            VerticalSpacer(150.dp)
            Icon(
                painter = painterResource(R.drawable.ic_logo_small),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.padding(horizontal = 100.dp)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                color = White1,
                style = TypoTheme.typography.displayLargeB.nonScaledSp,
            )
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { -it })
            ) {
                Text(
                    text = "나의 가장 좋은 지출관리 친구",
                    color = White1,
                    style = TypoTheme.typography.headlineMediumB.nonScaledSp
                )
            }
        }
        Text(
            text = stringResource(id = R.string.app_copyright),
            color = White1,
            style = TypoTheme.typography.titleSmallR.nonScaledSp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }

    LaunchedEffect(Unit) {
        visible = true
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    JunTheme {
        SplashScreen()
    }
}