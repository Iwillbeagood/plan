package jun.money.mate.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun FadeAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)),
        content = content,
        modifier = modifier,
    )
}

@Composable
inline fun <reified T> StateAnimatedVisibility(
    target: Any,
    modifier: Modifier = Modifier,
    noinline content: @Composable (T) -> Unit,
) {
    AnimatedVisibility(
        visible = target is T,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)),
        content = {
            if (target is T) {
                content(target)
            }
        },
        modifier = modifier,
    )
}

@Composable
fun ExpandAnimatedVisibility(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandIn(),
        exit = shrinkOut(),
        content = content,
    )
}

@Composable
fun LeftToRightSlideFadeAnimatedVisibility(
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(300),
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(300),
        ) + fadeOut(animationSpec = tween(300)),
        modifier = modifier,
        content = content,
    )
}

@Composable
fun BottomToTopAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content:
        @Composable()
        AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(300),
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(300),
        ),
        modifier = modifier,
        content = content,
    )
}

@Composable
fun BottomToTopAnimatedVisibility(
    time: Long,
    modifier: Modifier = Modifier,
    content:
        @Composable()
        AnimatedVisibilityScope.() -> Unit,
) {
    var isVisible by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(300),
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(300),
        ),
        modifier = modifier,
        content = content,
    )

    LaunchedEffect(true) {
        delay(time)
        isVisible = true
    }
}

@Composable
fun BottomToTopSlideFadeAnimatedVisibility(
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(300),
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(300),
        ) + fadeOut(animationSpec = tween(300)),
        modifier = modifier,
        content = content,
    )
}

@Composable
fun TopToBottomAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    duration: Int = 600,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(duration),
        ) + fadeIn(animationSpec = tween(duration)),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(duration),
        ) + fadeOut(animationSpec = tween(duration)),
        modifier = modifier,
        content = content,
    )
}

@Composable
fun TopToBottomAnimatedVisibility(
    time: Long,
    modifier: Modifier = Modifier,
    duration: Int = 600,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    var isVisible by remember { mutableStateOf(false) }
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(duration),
        ) + fadeIn(animationSpec = tween(duration)),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(duration),
        ) + fadeOut(animationSpec = tween(duration)),
        modifier = modifier,
        content = content,
    )

    LaunchedEffect(true) {
        delay(time)
        isVisible = true
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> CrossfadeWithSlide(
    targetState: T,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 500),
    label: String = "CrossfadeWithSlide",
    content: @Composable (T) -> Unit,
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            (
                fadeIn(
                    animationSpec,
                ) + slideInVertically { fullHeight -> fullHeight / 2 }
            ).togetherWith(
                fadeOut(animationSpec) + slideOutVertically { fullHeight -> fullHeight / 2 },
            )
        },
        modifier = modifier,
        label = label,
    ) { state ->
        content(state)
    }
}
