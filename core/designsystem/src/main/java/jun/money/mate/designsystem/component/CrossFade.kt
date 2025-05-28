package jun.money.mate.designsystem.component

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun <T> CrossfadeIfStateChanged(
    targetState: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    var previousState by remember { mutableStateOf(targetState) }

    if (previousState != targetState) {
        previousState = targetState
        Crossfade(
            targetState = targetState,
            modifier = modifier,
            content = content,
            label = "",
        )
    } else {
        content(targetState)
    }
}
