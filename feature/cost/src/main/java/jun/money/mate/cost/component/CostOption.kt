package jun.money.mate.cost.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem.theme.main

internal enum class CostOption {
    일반,
    구독,
    기타
}

@Composable
internal fun CostOptionTab(
    selectedOption: CostOption,
    onTabClick: (CostOption) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = TypoTheme.typography.titleSmallM
) {
    Surface(
        shape = CircleShape,
        color =  White1,
        border = BorderStroke(1.dp, Gray6),
        modifier = modifier.fillMaxWidth()
    ) {
        TabRow(
            selectedTabIndex = selectedOption.ordinal,
            containerColor = White1,
            contentColor = MaterialTheme.colorScheme.onSurface,
            indicator = { tabPositions ->
                Box(
                ) {
                    Surface(
                        shape = CircleShape,
                        color =  main.copy(alpha = 0.3f),
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedOption.ordinal])
                            .align(Alignment.Center)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "",
                            style = style,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            },
            divider = {}
        ) {
            CostOption.entries.forEachIndexed { tabIndex, tab ->
                Tab(
                    selected = selectedOption.ordinal == tabIndex,
                    onClick = { onTabClick(tab) },
                    text = {
                        Text(
                            text = tab.name,
                            style = if (selectedOption.ordinal == tabIndex) {
                                style.copy(fontWeight = FontWeight.Bold)
                            } else {
                                style
                            },
                            color = if (selectedOption.ordinal == tabIndex) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                main
                            },
                            maxLines = 1,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    },
                )
            }
        }
    }

}

@Preview
@Composable
private fun LoginOptionTabPreview() {
    JunTheme {
        CostOptionTab(
            selectedOption = CostOption.구독,
            onTabClick = {}
        )
    }
}