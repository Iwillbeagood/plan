package jun.money.mate.navigation

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
        data object Add : Income

        @Serializable
        data class Edit(val id: Long): Income
    }

    @Serializable
    sealed interface Save : Route {

        @Serializable
        data object List : Save

        @Serializable
        data class Detail(val id: Long) : Save

        @Serializable
        data object Add : Save

        @Serializable
        data class Edit(val id: Long): Save
    }

    @Serializable
    sealed interface Challenge : Route {

        @Serializable
        data class Detail(val id: Long) : Challenge

        @Serializable
        data object Add : Challenge
    }
}

sealed interface MainTabRoute : Route {

    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object Calendar : MainTabRoute

    @Serializable
    data object Finance : MainTabRoute


    @Serializable
    sealed interface ConsumptionSpend : MainTabRoute {

        @Serializable
        data object List : ConsumptionSpend

        @Serializable
        data class Add(val addType: AddType) : ConsumptionSpend
    }


    @Serializable
    sealed interface SpendingPlan : MainTabRoute {

        @Serializable
        data object List : SpendingPlan

        @Serializable
        data class Add(val addType: AddType) : SpendingPlan
    }
}

