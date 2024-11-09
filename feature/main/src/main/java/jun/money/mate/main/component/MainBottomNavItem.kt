package jun.money.mate.main.component

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Savings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.Route
import jun.money.mate.res.R

enum class MainBottomNavItem(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val route: MainTabRoute
) {
    Home(
        titleRes = R.string.tab_home,
        icon = Icons.Default.Home,
        route = MainTabRoute.Home
    ),
    SpendingList(
        titleRes = R.string.tab_spending_list,
        icon = Icons.Default.AttachMoney,
        route = MainTabRoute.SpendingList.List
    ),
    SavingPlan(
        titleRes = R.string.tab_saving_plan,
        icon = Icons.Default.Savings,
        route = MainTabRoute.SavingPlan
    ),
    SpendingPlan(
        titleRes = R.string.tab_spending_plan,
        icon = Icons.Default.CalendarToday,
        route = MainTabRoute.SpendingPlan.List
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