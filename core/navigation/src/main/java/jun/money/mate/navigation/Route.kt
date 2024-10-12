package jun.money.mate.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    sealed interface Income : Route {

        @Serializable
        data object List : Income

        @Serializable
        data object Add : Income
    }

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