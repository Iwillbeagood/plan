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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Gray1
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme

@Composable
fun TopAppbar(
    modifier: Modifier = Modifier,
    title: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    navigationType: TopAppbarType = TopAppbarType.Default,
    onBackEvent: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(backgroundColor)
            .statusBarsPadding()
            .padding(horizontal = 8.dp),
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TopAppbarIcon(
                icon = Icons.Default.ArrowBackIosNew,
                onClick = onBackEvent,
                tint = MaterialTheme.colorScheme.onSurface,
            )
            VerticalSpacer(16.dp)
            Text(
                text = title,
                style = TypoTheme.typography.titleNormalB,
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
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Gray1,
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .clickable(onClick = onClick),
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = "menu Icon",
            tint = tint,
            modifier = modifier.align(Alignment.Center),
        )
    }
}

@Composable
fun TopAppbarIcon(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Gray1,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "menu Icon",
            tint = tint,
            modifier = modifier.align(Alignment.Center),
        )
    }
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
            title = "등록",
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
                    text = "테스트",
                )
            },
        )
    }
}
