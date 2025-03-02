package jun.money.mate.income.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.error.MessageType

@Stable
internal sealed interface IncomeEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : IncomeEffect

    @Immutable
    data object IncomeComplete : IncomeEffect

    @Immutable
    data object DismissKeyboard : IncomeEffect

    @Immutable
    data object RemoveTitleFocus : IncomeEffect
}
