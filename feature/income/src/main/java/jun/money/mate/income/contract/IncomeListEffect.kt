package jun.money.mate.income.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface IncomeListEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : IncomeListEffect

    @Immutable
    data class EditIncome(val id: Long) : IncomeListEffect
}