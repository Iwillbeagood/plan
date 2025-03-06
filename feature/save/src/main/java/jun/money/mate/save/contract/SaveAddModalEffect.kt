package jun.money.mate.save.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface SaveAddModalEffect {

    @Immutable
    data object Idle : SaveAddModalEffect

    @Immutable
    data object ShowNumberKeyboard : SaveAddModalEffect
}