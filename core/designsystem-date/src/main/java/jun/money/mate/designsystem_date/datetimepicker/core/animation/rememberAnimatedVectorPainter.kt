package jun.money.mate.designsystem_date.datetimepicker.core.animation

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.VectorComposable
import androidx.compose.ui.graphics.vector.VectorConfig
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.util.fastForEach
import kic.owner2.designsystem_date.datetimepicker.core.util.ExperimentalAnimationGraphicsApi

@ExperimentalAnimationGraphicsApi
@Composable
fun rememberAnimatedVectorPainter(
    animatedImageVector: AnimatedImageVector,
    atEnd: Boolean
): Painter {
    return rememberAnimatedVectorPainter(animatedImageVector, atEnd) { group, overrides ->
        RenderVectorGroup(group, overrides)
    }
}

@ExperimentalAnimationGraphicsApi
@VisibleForTesting
@Composable
internal fun rememberAnimatedVectorPainter(
    animatedImageVector: AnimatedImageVector,
    atEnd: Boolean,
    render: @Composable @VectorComposable (VectorGroup, Map<String, VectorConfig>) -> Unit
): Painter {
    return rememberVectorPainter(
        defaultWidth = animatedImageVector.imageVector.defaultWidth,
        defaultHeight = animatedImageVector.imageVector.defaultHeight,
        viewportWidth = animatedImageVector.imageVector.viewportWidth,
        viewportHeight = animatedImageVector.imageVector.viewportHeight,
        name = animatedImageVector.imageVector.name,
        tintColor = animatedImageVector.imageVector.tintColor,
        tintBlendMode = animatedImageVector.imageVector.tintBlendMode,
        autoMirror = true
    ) { _, _ ->
        val transition = updateTransition(atEnd, label = animatedImageVector.imageVector.name)
        val map = mutableMapOf<String, StateVectorConfig>()
        animatedImageVector.targets.fastForEach { target ->
            val config = target.animator.createVectorConfig(
                transition,
                animatedImageVector.totalDuration
            )
            val currentConfig = map[target.name]
            if (currentConfig != null) {
                currentConfig.merge(config)
            } else {
                map[target.name] = config
            }
        }
        render(animatedImageVector.imageVector.root, map)
    }
}
