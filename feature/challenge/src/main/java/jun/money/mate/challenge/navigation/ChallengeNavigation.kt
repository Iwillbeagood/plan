package jun.money.mate.challenge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jun.money.mate.challenge.ChallengeAddRoute
import jun.money.mate.challenge.ChallengeRoute
import jun.money.mate.navigation.Route

fun NavController.navigateToChallengeDetail(id: Long) {
    navigate(Route.Challenge.Detail(id))
}

fun NavController.navigateToChallengeAdd() {
    navigate(Route.Challenge.Add)
}

fun NavGraphBuilder.challengeNavGraph() {
    composable<Route.Challenge.Detail> {
        ChallengeRoute()
    }
    composable<Route.Challenge.Add> {
        ChallengeAddRoute()
    }
}
