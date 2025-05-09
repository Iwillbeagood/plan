package jun.money.mate.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.nonScaledSp
import jun.money.mate.navigation.MainBottomNavItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainBottomBar(
    visible: Boolean,
    bottomItems: PersistentList<MainBottomNavItem>,
    currentItem: MainBottomNavItem?,
    onBottomItemClicked: (MainBottomNavItem) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) }
    ) {
        Column {
            HorizontalDivider()
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier.height(65.dp)
            ) {
                bottomItems.forEach { item ->
                    val title = stringResource(item.titleRes)
    
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = title,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = item.color,
                            selectedTextColor = item.color,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Gray,
                            unselectedTextColor = Gray,
                        ),
                        label = {
                            Text(
                                text = title,
                                style = JUNTheme.typography.labelLargeM.nonScaledSp
                            )
                        },
                        onClick = { onBottomItemClicked(item) },
                        selected = item == currentItem
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainBottomBarPreview() {
    JunTheme {
        MainBottomBar(
            visible = true,
            bottomItems = MainBottomNavItem.entries.toPersistentList(),
            currentItem = MainBottomNavItem.Home,
            onBottomItemClicked = { }
        )
    }
}