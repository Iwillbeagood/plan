package jun.money.mate.save.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface SaveDetailModalEffect {

    @Immutable
    data object Hidden : SaveDetailModalEffect

    @Immutable
    data object ShowNumberKeyboard : SaveDetailModalEffect

    @Immutable
    data object ShowPeriodDeleteConfirmDialog : SaveDetailModalEffect

    @Immutable
    data object ShowBasicDeleteConfirmDialog : SaveDetailModalEffect
}