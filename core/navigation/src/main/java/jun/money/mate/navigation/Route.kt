package jun.money.mate.navigation

import android.annotation.SuppressLint
import jun.money.mate.navigation.Route.Income
import jun.money.mate.navigation.argument.AddType
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    sealed interface Income : Route {

        @Serializable
        data object List : Income

        @Serializable
        data class Add(val addType: AddType) : Income
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
    sealed interface SpendingPlan : MainTabRoute {

        @Serializable
        data object List : SpendingPlan

        @Serializable
        data class Add(val addType: AddType) : SpendingPlan
    }
}

