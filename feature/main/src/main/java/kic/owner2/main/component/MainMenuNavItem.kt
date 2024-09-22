package kic.owner2.main.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import kic.owner2.main.R
import jun.money.mate.navigation.MainMenuRoute
import jun.money.mate.navigation.Route

enum class MainMenuNavItem(
    @StringRes val titleRes: Int,
    val icon: Int,
    val route: MainMenuRoute
) {
    CustomerService(
        titleRes = kic.owner2.stringres.R.string.menu_customer_service,
        icon = R.drawable.ic_callcenter,
        route = MainMenuRoute.CustomerService
    ),
    MyList(
        titleRes = kic.owner2.stringres.R.string.menu_my_list,
        icon = R.drawable.ic_my_list,
        route = MainMenuRoute.MyList
    ),
    Settlement(
        titleRes = kic.owner2.stringres.R.string.menu_settlement,
        icon = R.drawable.ic_graph,
        route = MainMenuRoute.Settlement
    ),
    Deposit(
        titleRes = kic.owner2.stringres.R.string.menu_deposit,
        icon = R.drawable.ic_deposit,
        route = MainMenuRoute.Deposit
    ),
    Mileage(
        titleRes = kic.owner2.stringres.R.string.menu_mileage,
        icon = kic.owner2.designsystem.R.drawable.ic_mileage,
        route = MainMenuRoute.Mileage
    ),
    Notice(
        titleRes = kic.owner2.stringres.R.string.menu_notice,
        icon = kic.owner2.designsystem.R.drawable.ic_notice,
        route = MainMenuRoute.Notice
    ),
    Setting(
        titleRes = kic.owner2.stringres.R.string.menu_setting,
        icon = kic.owner2.designsystem.R.drawable.ic_setting,
        route = MainMenuRoute.Setting.Main
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainMenuRoute) -> Boolean): MainMenuNavItem? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}