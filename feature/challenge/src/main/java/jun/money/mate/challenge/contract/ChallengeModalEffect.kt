package jun.money.mate.challenge.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface ChallengeModalEffect {

    @Immutable
    data object Idle : ChallengeModalEffect

    @Immutable
    data object ShowNumberKeyboard : ChallengeModalEffect
}
