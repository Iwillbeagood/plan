package jun.money.mate.income.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface IncomeModalEffect {

    @Immutable
    data object Idle : IncomeModalEffect

    @Immutable
    data object ShowNumberKeyboard : IncomeModalEffect
}
