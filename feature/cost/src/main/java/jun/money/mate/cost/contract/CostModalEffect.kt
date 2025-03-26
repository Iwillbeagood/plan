package jun.money.mate.cost.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface CostModalEffect {

    @Immutable
    data object Hidden : CostModalEffect

    @Immutable
    data object ShowDeleteDialog : CostModalEffect
}