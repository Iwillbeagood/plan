package jun.money.mate.save.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface SaveModalEffect {

    @Immutable
    data object Hidden : SaveModalEffect

    @Immutable
    data object ShowDeleteConfirmDialog : SaveModalEffect
}