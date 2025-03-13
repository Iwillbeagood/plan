package jun.money.mate.challenge.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface SaveAddEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : SaveAddEffect

    @Immutable
    data object SaveAddComplete : SaveAddEffect

    @Immutable
    data object RemoveTextFocus : SaveAddEffect
}