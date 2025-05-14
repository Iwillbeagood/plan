package jun.money.mate.challenge.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface ChallengeAddEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : ChallengeAddEffect

    @Immutable
    data object ChallengeAddComplete : ChallengeAddEffect

    @Immutable
    data object RemoveTextFocus : ChallengeAddEffect

    @Immutable
    data object ScrollToBottom : ChallengeAddEffect
}
