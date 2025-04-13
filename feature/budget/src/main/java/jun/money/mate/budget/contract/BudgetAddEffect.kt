package jun.money.mate.budget.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface BudgetAddEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : BudgetAddEffect

    @Immutable
    data object BudgetAddComplete : BudgetAddEffect

    @Immutable
    data object DismissKeyboard : BudgetAddEffect

    @Immutable
    data object RemoveTitleFocus : BudgetAddEffect
}
