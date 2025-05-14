package jun.money.mate.challenge.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.save.Challenge

@Stable
internal sealed interface ChallengeState {

    @Immutable
    data object Loading : ChallengeState

    @Immutable
    data class ChallengeData(
        val challenge: Challenge,
    ) : ChallengeState
}
