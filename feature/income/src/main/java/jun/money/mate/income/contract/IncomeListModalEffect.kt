package jun.money.mate.income.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface IncomeListModalEffect {

    @Immutable
    data object Hidden : IncomeListModalEffect

    @Immutable
    data object ShowDeleteConfirmDialog : IncomeListModalEffect
}
