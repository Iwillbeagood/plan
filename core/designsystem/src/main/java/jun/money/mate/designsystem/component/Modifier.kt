package jun.money.mate.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Gray7

@Stable
fun Modifier.circleBackground(
    color: Color = Gray7,
    padding: Dp = 5.dp,
): Modifier {
    return this
        .clip(CircleShape)
        .background(color)
        .padding(padding)
}
