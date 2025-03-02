package jun.money.mate.income.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.model.Leaf
import jun.money.mate.model.LeafOrder
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
internal fun LeavesBox(
    leaves: List<LeafOrder>,
    modifier: Modifier = Modifier
) {
    FallingLeaves(
        leaves = leaves,
        modifier = modifier
    )
}

@Composable
private fun FallingLeaves(
    leaves: List<LeafOrder>,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.run { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val leafSize = 50f

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        leaves.forEachIndexed { index, leafOrder ->
            val leaf = Leaf(
                startX = Random.nextFloat() * (density - leafSize),
                startY = -50f,
                endY = -50f,
                swingAmount = Random.nextFloat() * 200f - 100f,
                isRed = leafOrder.isRed
            )

            val animatedY = remember { Animatable(leaf.startY) }
            val animatedX = remember { Animatable(leaf.startX) }
            val isFalling = remember { mutableStateOf(true) }

            LaunchedEffect(index) {
                launch {
                    val endYPosition = Random.nextFloat() * 1000f
                    animatedY.animateTo(
                        targetValue = endYPosition,
                        animationSpec = tween(
                            durationMillis = 3000,
                            easing = LinearEasing
                        )
                    )
                    isFalling.value = false
                }

                launch {
                    if (isFalling.value) {
                        animatedX.animateTo(
                            targetValue = leaf.startX + leaf.swingAmount,
                            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                        )
                        animatedX.animateTo(
                            targetValue = leaf.startX - leaf.swingAmount,
                            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                        )
                    }
                }
            }
            LeafIcon(
                isRed = leaf.isRed,
                modifier = Modifier
                    .offset { IntOffset(animatedX.value.toInt(), animatedY.value.toInt()) }
                    .size(50.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MoneyBoxPreview() {
    JunTheme {
        LeavesBox(
            leaves = listOf(LeafOrder(false)),
            modifier = Modifier.fillMaxHeight()
        )
    }
}