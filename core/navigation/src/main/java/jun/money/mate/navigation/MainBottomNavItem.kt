package jun.money.mate.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Savings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.Orange1
import jun.money.mate.designsystem.theme.Purple1
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem.theme.main
import jun.money.mate.res.R

// 홈, 캘린더, 소비, 수입과 저금
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
    Calendar(
        titleRes = R.string.tab_calendar,
        icon = Icons.Default.AccountBalanceWallet,
        route = MainTabRoute.Income.List,
    ),
    Finance(
        titleRes = R.string.tab_finance,
        icon = Icons.Default.AccountBalanceWallet,
        route = MainTabRoute.Income.List,
    ),
    Budget(
        titleRes = R.string.tab_budget,
        icon = Icons.Default.AttachMoney,
        route = MainTabRoute.SpendingPlan.List,
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