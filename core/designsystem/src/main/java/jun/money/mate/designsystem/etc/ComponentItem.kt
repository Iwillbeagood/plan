package jun.money.mate.designsystem.etc

import androidx.compose.runtime.Immutable

@Immutable
data class ComponentItem(
    val text: String,
    val selected: Boolean = false,
) {
    companion object {
        fun List<String>.toComponentItems() = mapIndexed { index, s ->
            ComponentItem(
                text = s,
                selected = index == 0,
            )
        }
    }
}
