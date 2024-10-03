package jun.money.mate.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

}

sealed interface MainTabRoute : Route {

    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object SpendingList : MainTabRoute

    @Serializable
    data object SavingPlan : MainTabRoute

    @Serializable
    data object SpendingPlan : MainTabRoute

}