package jun.money.mate.challenge.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface ChallengeEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : ChallengeEffect

    @Immutable
    data class EditSpendingPlan(val id: Long) : ChallengeEffect
}