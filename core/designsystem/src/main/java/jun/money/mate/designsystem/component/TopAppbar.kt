package jun.money.mate.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.R
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.LightBlue2

@Composable
fun TopAppbar(
    modifier: Modifier = Modifier,
    title: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceDim,
    lineColor: Color = LightBlue2,
    navigationType: HmTopAppbarType = HmTopAppbarType.Default,
    onBackEvent: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(backgroundColor)
            .statusBarsPadding()
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable(onClick = onBackEvent)
            ) {
                TopAppbarIcon(
                    modifier = Modifier.align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onSurface,
                    iconId = R.drawable.ic_back
                )
            }
            Text(
                text = title,
                style = JUNTheme.typography.titleMediumM
            )
            Spacer(modifier = Modifier.weight(1f))

            when (navigationType) {
                HmTopAppbarType.Default -> {}
                is HmTopAppbarType.Custom -> {
                    navigationType.content()
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            lineColor = lineColor
        )
    }
}

@Composable
fun TopAppbarIcon(
    tint: Color,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(iconId),
        contentDescription = "menu Icon",
        tint = tint,
        modifier = modifier,
    )
}

sealed interface HmTopAppbarType {
    data object Default : HmTopAppbarType
    data class Custom(val content: @Composable () -> Unit) : HmTopAppbarType
}

@Preview(showBackground = true)
@Composable
fun BasicTopAppbarPreview() {
    JunTheme {
        TopAppbar(
            title = "화물등록"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTopAppbarPreview() {
    JunTheme {
        TopAppbar(
            title = "정산",
            navigationType = HmTopAppbarType.Custom {
                Text(
                    modifier = Modifier
                        .padding(end = 5.dp),
                    text = "테스트"
                )
            }
        )
    }
}


