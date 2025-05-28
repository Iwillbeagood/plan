package jun.money.mate.save.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface SaveDetailEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : SaveDetailEffect

    @Immutable
    data object SaveDetailComplete : SaveDetailEffect
}
