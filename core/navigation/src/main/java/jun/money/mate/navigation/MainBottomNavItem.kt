package jun.money.mate.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import jun.money.mate.res.R

enum class MainBottomNavItem(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val route: MainTabRoute,
) {
    Home(
        titleRes = R.string.tab_home,
        icon = Icons.Default.Home,
        route = MainTabRoute.Home,
    ),
    Cost(
        titleRes = R.string.tab_cost,
        icon = Icons.Default.AttachMoney,
        route = MainTabRoute.Cost.Main,
    ),
    Finance(
        titleRes = R.string.tab_finance,
        icon = Icons.Default.AccountBalanceWallet,
        route = MainTabRoute.Finance,
    ),
    Calendar(
        titleRes = R.string.tab_calendar,
        icon = Icons.Default.CalendarMonth,
        route = MainTabRoute.Calendar,
    );

    companion object {
        fun find(predicate: (MainTabRoute) -> Boolean): MainBottomNavItem? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}