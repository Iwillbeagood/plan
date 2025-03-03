package jun.money.mate.designsystem.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.JunTheme

@Composable
fun TabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit,
    style: TextStyle = TypoTheme.typography.titleMediumM
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surfaceDim,
            contentColor = MaterialTheme.colorScheme.onSurface,
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    SecondaryIndicator(
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                    )
                }
            },
            divider = {
                HorizontalDivider()
            }
        ) {
            tabs.forEachIndexed { tabIndex, tab ->
                Tab(
                    selected = selectedTabIndex == tabIndex,
                    onClick = { onTabClick(tabIndex) },
                    text = {
                        Text(
                            text = tab,
                            style = if (selectedTabIndex == tabIndex) {
                                style.copy(fontWeight = FontWeight.Medium)
                            } else {
                                style
                            },
                            color = if (selectedTabIndex == tabIndex) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                Gray6
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

@Composable
fun ScrollableTab(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = TypoTheme.typography.titleMediumM,
    containerColor: Color = MaterialTheme.colorScheme.surfaceDim
) {
    val density = LocalDensity.current
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabs.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    ScrollableTabRow(
        modifier = modifier.fillMaxWidth(),
        selectedTabIndex = selectedTabIndex,
        containerColor = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier.customTabIndicatorOffset(
                    currentTabPosition = tabPositions[selectedTabIndex],
                    tabWidth = tabWidths[selectedTabIndex]
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { tabIndex, tab ->
            Tab(
                selected = selectedTabIndex == tabIndex,
                onClick = { onTabClick(tabIndex) },
                text = {
                    Text(
                        text = tab,
                        style = if (selectedTabIndex == tabIndex) {
                            style.copy(fontWeight = FontWeight.Medium)
                        } else {
                            style
                        },
                        color = if (selectedTabIndex == tabIndex) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            Gray6
                        },
                        onTextLayout = { textLayoutResult ->
                            tabWidths[tabIndex] =
                                with(density) { textLayoutResult.size.width.toDp() }
                        }
                    )
                },
            )
        }
    }
}

private fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing), label = ""
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing), label = ""
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset, y = 0.dp)
        .width(currentTabWidth)
}

@Preview
@Composable
private fun TabPreview() {
    JunTheme {
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        TabRow(
            tabs = listOf("아이디 찾기", "비밀번호 찾기"),
            selectedTabIndex = selectedTabIndex,
            onTabClick = {
                selectedTabIndex = it
            }
        )
    }
}

@Preview
@Composable
private fun ScrollableTabPreview() {
    JunTheme {
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        ScrollableTab(
            tabs = listOf(
                "아이디 찾기",
                "비밀번호 찾기",
                "아이디 찾기",
                "비밀번호 찾기",
                "아이디 찾기",
                "비밀번호 찾기",
                "아이디 찾기",
                "비밀번호 찾기"
            ),
            selectedTabIndex = selectedTabIndex,
            onTabClick = {
                selectedTabIndex = it
            }
        )
    }
}