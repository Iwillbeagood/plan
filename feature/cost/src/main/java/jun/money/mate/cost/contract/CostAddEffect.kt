package jun.money.mate.cost.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface CostAddEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : CostAddEffect

    @Immutable
    data object CostAddComplete : CostAddEffect

    @Immutable
    data object RemoveTextFocus : CostAddEffect
}