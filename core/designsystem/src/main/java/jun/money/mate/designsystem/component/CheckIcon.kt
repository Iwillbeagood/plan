package jun.money.mate.designsystem.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem.theme.main

@Composable
fun CheckIcon(
    modifier: Modifier = Modifier
) {
    var checked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        checked = true
    }

    Surface(
        color = main,
        shape = CircleShape,
        modifier = modifier.size(24.dp)
    ) {
        AnimatedCheckIcon(
            isChecked = checked,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun AnimatedCheckIcon(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    color: Color = White1,
    strokeWidth: Float = 8f
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (isChecked) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "CheckAnimation"
    )

    Canvas(modifier = modifier.size(24.dp)) {
        val startX = size.width * 0.2f
        val startY = size.height * 0.5f
        val midX = size.width * 0.45f
        val midY = size.height * 0.75f
        val endX = size.width * 0.8f
        val endY = size.height * 0.3f

        val firstSegmentEndX = startX + (midX - startX) * animationProgress
        val firstSegmentEndY = startY + (midY - startY) * animationProgress

        val secondSegmentEndX = midX + (endX - midX) * animationProgress
        val secondSegmentEndY = midY + (endY - midY) * animationProgress

        drawLine(
            color = color,
            start = Offset(startX, startY),
            end = Offset(firstSegmentEndX, firstSegmentEndY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        if (animationProgress > 0.5f) {
            drawLine(
                color = color,
                start = Offset(midX, midY),
                end = Offset(secondSegmentEndX, secondSegmentEndY),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

@Preview
@Composable
private fun AnimatedCheckIcon() {
    JunTheme {
        CheckIcon()
    }
}