package jun.money.mate.budget.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface BudgetDetailEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : BudgetDetailEffect

    @Immutable
    data object PopBackStack : BudgetDetailEffect
}