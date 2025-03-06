package jun.money.mate.save.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.res.R
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

private data class Acorn(
    val x: Float,
    val count: Int,
    val rotation: Float,
    val isGold: Boolean = false
)

@Composable
internal fun AcornBox(
    modifier: Modifier = Modifier,
    goldCount: Int = 0,
    count: Int = 20,
    size: Int = 10,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp - 50
    val spacing = 20f
    val centerX = screenWidth / 2f

    val xPositions = List(size) { i -> centerX - ((size - 1) / 2f) * spacing + i * spacing }

    val maxHeight = (size / 2) + 1
    val maxHeightMap = xPositions.associateWith { index ->
        when (index) {
            xPositions.first(), xPositions.last() -> 1
            else -> maxHeight - abs(xPositions.indexOf(index) - (size / 2))
        }
    }

    var acorns by remember { mutableStateOf(listOf<Acorn>()) }
    val yOffsetMap = remember { mutableStateMapOf<Float, Int>() }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        acorns.forEachIndexed { _, (x, count, rotation, isGold) ->
            val animatedY = remember { Animatable(-50f) }

            LaunchedEffect(Unit) {
                animatedY.animateTo(
                    targetValue = 300f - (count * 25f),
                    animationSpec = tween(durationMillis = Random.nextInt(1000, 3000), easing = EaseOutBounce)
                )
            }

            Image(
                painter = painterResource(id = if (isGold) R.drawable.ic_acorn_gold else R.drawable.ic_acorn),
                contentDescription = "도토리",
                modifier = Modifier
                    .size(50.dp)
                    .absoluteOffset(x.dp, animatedY.value.dp)
                    .graphicsLayer(rotationZ = rotation)
            )
        }
    }

    LaunchedEffect(count, goldCount) {
        acorns = emptyList()
        yOffsetMap.clear()

        repeat(count) {
            val randomX = xPositions.random()
            val currentCount = yOffsetMap[randomX] ?: 0
            val maxCount = maxHeightMap[randomX] ?: 4

            if (currentCount < maxCount) {
                yOffsetMap[randomX] = currentCount + 1
                val randomRotation = (0..360).random().toFloat()
                acorns = acorns + Acorn(randomX, currentCount + 1, randomRotation)
            }
            delay(30L)
        }

        repeat(goldCount) {
            val randomX = xPositions.random()
            val currentCount = yOffsetMap[randomX] ?: 0
            val maxCount = maxHeightMap[randomX] ?: 4

            if (currentCount < maxCount) {
                yOffsetMap[randomX] = currentCount + 1
                val randomRotation = (0..360).random().toFloat()
                acorns = acorns + Acorn(randomX, currentCount + 1, randomRotation, true)
            }
            delay(30L)
        }
    }
}

@Preview
@Composable
private fun AcornBoxPreview() {
    JunTheme {
        AcornBox(count = 20)
    }
}