package jun.money.mate.save.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
    val imageId: Int
)

@Composable
internal fun AcornFlowerBox(
    modifier: Modifier = Modifier,
    goldCount: Int = 0,
    count: Int = 20,
    flowerCount: Int = 0,
    size: Int = 10,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp - 50
    val screenHeight = configuration.screenHeightDp
    val maxDropHeight = screenHeight * 0.32f

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
            .height((screenHeight * 0.4f).dp)
    ) {
        acorns.forEachIndexed { _, (x, count, rotation, iconId) ->
            val animatedY = remember { Animatable(-50f) }

            LaunchedEffect(maxDropHeight) { // 40% 높이에서만 애니메이션 동작
                animatedY.animateTo(
                    targetValue = maxDropHeight - (count * 10f), // 최대 높이를 40%로 제한
                    animationSpec = tween(
                        durationMillis = Random.nextInt(1000, 3000),
                        easing = EaseOutBounce
                    )
                )
            }

            Image(
                painter = painterResource(id = iconId),
                contentDescription = "도토리",
                modifier = Modifier
                    .size(50.dp)
                    .absoluteOffset(x.dp, animatedY.value.dp)
                    .graphicsLayer(rotationZ = rotation)
            )
        }
    }

    LaunchedEffect(count, goldCount, flowerCount) {
        acorns = emptyList()
        yOffsetMap.clear()

        repeat(count) {
            val randomX = xPositions.random()
            val currentCount = yOffsetMap[randomX] ?: 0
            val maxCount = maxHeightMap[randomX] ?: 4

            if (currentCount < maxCount) {
                yOffsetMap[randomX] = currentCount + 1
                val randomRotation = (0..360).random().toFloat()
                acorns = acorns + Acorn(randomX, currentCount + 1, randomRotation, R.drawable.ic_acorn)
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
                acorns = acorns + Acorn(randomX, currentCount + 1, randomRotation, R.drawable.ic_acorn_gold)
            }
            delay(30L)
        }

        repeat(flowerCount) {
            val randomX = xPositions.random()
            val currentCount = yOffsetMap[randomX] ?: 0
            val maxCount = maxHeightMap[randomX] ?: 4

            if (currentCount < maxCount) {
                yOffsetMap[randomX] = currentCount + 1
                val randomRotation = (0..360).random().toFloat()
                acorns = acorns + Acorn(randomX, currentCount + 1, randomRotation, R.drawable.ic_smile_flower)
            }
            delay(30L)
        }
    }
}

@Preview
@Composable
private fun AcornBoxPreview() {
    JunTheme {
        AcornFlowerBox(count = 20)
    }
}