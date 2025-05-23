package jun.money.mate.designsystem_date.datetimepicker.core.animation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.util.fastMaxBy
import jun.money.mate.designsystem_date.datetimepicker.core.util.ExperimentalAnimationGraphicsApi

/**
 * Animated vector graphics object that is generated as a result of
 * [androidx.compose.animation.graphics.res.loadAnimatedVectorResource].
 * It can be composed and rendered by `rememberAnimatedVectorPainter`.
 *
 * @param imageVector The [ImageVector] to be animated. This is represented with the
 * `android:drawable` parameter of an `<animated-vector>` element.
 */
@ExperimentalAnimationGraphicsApi
@Immutable
class AnimatedImageVector internal constructor(
    val imageVector: ImageVector,
    // The list of [AnimatedVectorTarget]s that specify animations for each of the elements in the
    // drawable. This is represented with `<target>` elements in `<animated-vector>`. This list is
    // expected to be *immutable*.
    internal val targets: List<AnimatedVectorTarget>
) {

    /**
     * The total duration of all the animations in this image, including start delays and repeats.
     */
    val totalDuration = targets.fastMaxBy {
        it.animator.totalDuration
    }?.animator?.totalDuration ?: 0

    /**
     * Provide an empty companion object to hang platform-specific companion extensions onto.
     */
    companion object {} // ktlint-disable no-empty-class-body
}

/**
 * Definition of animation to one of the elements in a [AnimatedImageVector].
 */
@Immutable
internal class AnimatedVectorTarget(
    val name: String,
    val animator: Animator
)
