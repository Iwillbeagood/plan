package kic.owner2.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.White1

@Composable
internal fun MenuDrawerSheet(
    onDismiss: () -> Unit,
    navigateTo: (MainMenuNavItem) -> Unit,
    items: List<MainMenuNavItem> = MainMenuNavItem.entries
) {
    val localDensity = LocalDensity.current
    var tabWidth by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(140.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onDismiss)
                .padding(horizontal = 15.dp)
                .height(50.dp)

        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                tint = White1,
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .align(Alignment.CenterStart)
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
        )
        items.forEachIndexed { index, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .clickable {
                        navigateTo(item)
                        onDismiss()
                    }
                    .padding(start = 20.dp)
                    .onGloballyPositioned { coordinates ->
                        tabWidth = with(localDensity) { coordinates.size.width.toDp() }
                    }
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    tint = White1,
                    contentDescription = stringResource(id = item.titleRes),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = stringResource(id = item.titleRes),
                    color = White1,
                    style = JUNTheme.typography.titleMediumM,
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun MenuDrawerSheetPreview() {
    JunTheme {
        MenuDrawerSheet(
            {},
            navigateTo = {}
        )
    }
}