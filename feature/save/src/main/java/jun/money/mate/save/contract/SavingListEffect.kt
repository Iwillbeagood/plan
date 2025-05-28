package jun.money.mate.save.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface SavingListEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : SavingListEffect

    @Immutable
    data class EditSpendingPlan(val id: Long) : SavingListEffect
}
