package jun.money.mate.designsystem_date.datetimepicker.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

private const val TABLET_THRESHOLD = 800

/**
 * Determines whether the current screen should use landscape mode.
 *
 * @return `true` if the screen height is less than the [TABLET_THRESHOLD] in landscape mode, `false` otherwise.
 */
@Composable
fun shouldUseLandscape(): Boolean =
    LocalConfiguration.current.screenHeightDp < TABLET_THRESHOLD