package jun.money.mate.cost.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface CostEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : CostEffect

    @Immutable
    data class NavigateToCostDetail(val id: Long) : CostEffect
}
