package jun.money.mate.main.component

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Savings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem.theme.Yellow1
import jun.money.mate.designsystem.theme.main
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.Route
import jun.money.mate.res.R

enum class MainBottomNavItem(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val route: MainTabRoute,
    val color: Color
) {
    Home(
        titleRes = R.string.tab_home,
        icon = Icons.Default.Home,
        route = MainTabRoute.Home,
        color = Black
    ),
    SpendingPlan(
        titleRes = R.string.tab_spending_plan,
        icon = Icons.Default.CalendarToday,
        route = MainTabRoute.SpendingPlan.List,
        color = Red3
    ),
    Income(
        titleRes = R.string.tab_income,
        icon = Icons.Default.AttachMoney,
        route = MainTabRoute.Income.List,
        color = main
    ),
    ConsumptionSpend(
        titleRes = R.string.tab_spending_list,
        icon = Icons.Default.AddCard,
        route = MainTabRoute.ConsumptionSpend.List,
        color = Red3
    ),
    SavingPlan(
        titleRes = R.string.tab_saving_plan,
        icon = Icons.Default.Savings,
        route = MainTabRoute.SavingPlan.List,
        color = Yellow1
    ),
    ;

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