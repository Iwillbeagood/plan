package jun.money.mate.challenge.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import kotlinx.coroutines.launch
import kotlin.math.sin

@Composable
internal fun WaterProgress(
    currentAmount: Long,
    goalAmount: Long,
    modifier: Modifier = Modifier,
) {
    val challengeRatio = if (goalAmount == 0L && currentAmount > 0L) {
        1f
    } else if (goalAmount > 0L) {
        currentAmount.toFloat() / goalAmount.toFloat()
    } else {
        0f
    }

    val animatedProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val waveOffset = remember { Animatable(0f) }

    // 물 차오르는 애니메이션
    LaunchedEffect(challengeRatio) {
        coroutineScope.launch {
            animatedProgress.animateTo(
                targetValue = challengeRatio,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            )
        }
    }

    // 물결 애니메이션 (좌우로 움직이는 효과)
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (true) {
                waveOffset.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse,
                    ),
                )
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        // 퍼센트 텍스트

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.BottomCenter),
        ) {
            val waveHeight = 15f // 물결 높이 (크기 조절 가능)
            val waveWidth = size.width / 4 // 물결 폭
            val waterHeight = size.height * animatedProgress.value // **정확한 물 높이 반영**

            val path = Path().apply {
                moveTo(0f, size.height - waterHeight) // 물 높이 정확히 적용
                for (i in 0..size.width.toInt() step waveWidth.toInt()) {
                    val x = i.toFloat()
                    val y = size.height - waterHeight + waveHeight * sin((x / waveWidth * 2 * Math.PI + waveOffset.value * 2 * Math.PI).toFloat())
                    lineTo(x, y)
                }
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            drawPath(
                path = path,
                color = Color.Blue.copy(alpha = 0.6f),
            )
        }
        val percent = (animatedProgress.value * 100).toInt()
        Text(
            text = "$percent%",
            style = TypoTheme.typography.titleSmallM,
            color = if (percent > 50) Color.White else Color.Black,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center),
        )
    }
}

@Preview
@Composable
private fun WaterProgressPreview() {
    JunTheme {
        WaterProgress(
            goalAmount = 100000L,
            currentAmount = 60000L,
            modifier = Modifier,
        )
    }
}
