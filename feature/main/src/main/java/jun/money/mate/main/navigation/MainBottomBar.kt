package jun.money.mate.main.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.Gray10
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.nonScaledSp
import jun.money.mate.navigation.MainBottomNavItem
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainBottomBar(
    visible: Boolean,
    bottomItems: PersistentList<MainBottomNavItem>,
    currentItem: MainBottomNavItem?,
) {
    val navigateAction = LocalNavigateActionInterop.current
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) },
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .border(1.dp, Gray10, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .shadow(1.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .height(60.dp),
        ) {
            bottomItems.forEach { item ->
                val title = stringResource(item.titleRes)

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = title,
                            modifier = Modifier.size(22.dp),
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Black,
                        selectedTextColor = Black,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Gray6,
                        unselectedTextColor = Gray6,
                    ),
                    label = {
                        Text(
                            text = title,
                            style = TypoTheme.typography.labelMediumM.nonScaledSp,
                        )
                    },
                    onClick = { navigateAction.navigateBottomNav(item) },
                    selected = item == currentItem,
                )
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
        )
    }
}
