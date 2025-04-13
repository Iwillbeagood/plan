package jun.money.mate.budget.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface BudgetAddModalEffect {

    @Immutable
    data object Idle : BudgetAddModalEffect

    @Immutable
    data object ShowNumberKeyboard : BudgetAddModalEffect
}