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
import jun.money.mate.designsystem.theme.TypoTheme

@Composable
fun TopAppbar(
    modifier: Modifier = Modifier,
    title: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    navigationType: TopAppbarType = TopAppbarType.Default,
    onBackEvent: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(backgroundColor)
            .statusBarsPadding()
            .padding(end = 10.dp)
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (navigationType == TopAppbarType.Default) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable(onClick = onBackEvent)
                ) {
                    TopAppbarIcon(
                        iconId = R.drawable.ic_back,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            } else {
                HorizontalSpacer(20.dp)
            }
            Text(
                text = title,
                style = TypoTheme.typography.titleNormalB
            )
            Spacer(modifier = Modifier.weight(1f))

            when (navigationType) {
                TopAppbarType.Default -> {}
                is TopAppbarType.Custom -> {
                    navigationType.content()
                }
            }
        }
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

sealed interface TopAppbarType {
    data object Default : TopAppbarType
    data class Custom(val content: @Composable () -> Unit) : TopAppbarType
}

@Preview(showBackground = true)
@Composable
fun BasicTopAppbarPreview() {
    JunTheme {
        TopAppbar(
            title = "등록"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTopAppbarPreview() {
    JunTheme {
        TopAppbar(
            title = "정산",
            navigationType = TopAppbarType.Custom {
                Text(
                    modifier = Modifier
                        .padding(end = 5.dp),
                    text = "테스트"
                )
            }
        )
    }
}


