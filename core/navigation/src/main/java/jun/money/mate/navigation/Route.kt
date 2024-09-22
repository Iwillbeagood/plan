package jun.money.mate.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    data object CargoRegistration : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object Push : Route

    @Serializable
    data object MyInfo : Route

    @Serializable
    data class CargoDetail(val cargoId: String) : Route
}

sealed interface MainMenuRoute : Route {

    @Serializable
    data object CustomerService : MainMenuRoute
    @Serializable
    data object MyList : MainMenuRoute
    @Serializable
    data object Settlement : MainMenuRoute
    @Serializable
    data object Deposit : MainMenuRoute
    @Serializable
    data object Mileage : MainMenuRoute
    @Serializable
    data object Notice : MainMenuRoute

    @Serializable
    sealed interface Setting : MainMenuRoute {
        @Serializable
        data object Main : Setting
        @Serializable
        data object Push : Setting
    }
}