package jun.money.mate.designsystem_date.datetimepicker.core.base

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kic.owner2.designsystem_date.datetimepicker.core.util.shouldUseLandscape
import kic.owner2.designsystem_date.datetimepicker.models.BaseConfigs
import kic.owner2.designsystem_date.datetimepicker.models.LibOrientation

/**
 * Base component for the content structure of a dialog.
 * @param header The content to be displayed inside the dialog that functions as the header view of the dialog.
 * @param horizontalContentPadding The horizontal padding that is applied to the content.
 * @param layout The content to be displayed inside the dialog between the header and the buttons.
 * @param layoutHorizontalAlignment The horizontal alignment of the layout's children.
 * @param layoutLandscape The content to be displayed inside the dialog between the header and the buttons when the device is in landscape mode.
 * @param layoutLandscapeVerticalAlignment The vertical alignment of the layout's children in landscape mode.
 * @param buttonsVisible Display the buttons.
 * @param buttons The content to be displayed inside the dialog that functions as the buttons view of the dialog.
 */
@Composable
fun FrameBase(
    config: BaseConfigs? = null,
    layout: @Composable ColumnScope.(LibOrientation) -> Unit,
    layoutHorizontalAlignment: Alignment.Horizontal = Alignment.Start,
    layoutLandscape: @Composable (RowScope.() -> Unit)? = null,
    layoutLandscapeVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    buttonsVisible: Boolean = true,
    buttons: @Composable (ColumnScope.(LibOrientation) -> Unit)? = null,
) {
    val shouldUseLandscapeLayout = shouldUseLandscape()
    val currentOrientation = LocalConfiguration.current.orientation
    val isDeviceLandscape = currentOrientation == Configuration.ORIENTATION_LANDSCAPE
    val deviceOrientation =
        if (config?.orientation != LibOrientation.PORTRAIT && isDeviceLandscape) LibOrientation.LANDSCAPE else LibOrientation.PORTRAIT
    val layoutType = when (config?.orientation) {
        null -> {
            when {
                // Only if auto orientation is currently landscape, content for landscape exists
                // and the device screen is not larger than a typical phone.
                isDeviceLandscape
                        && layoutLandscape != null
                        && shouldUseLandscapeLayout -> LibOrientation.LANDSCAPE
                else -> LibOrientation.PORTRAIT
            }
        }
        LibOrientation.LANDSCAPE -> if (layoutLandscape != null) LibOrientation.LANDSCAPE else LibOrientation.PORTRAIT
        else -> config.orientation
    }

    Column(
        modifier = when (deviceOrientation) {
            LibOrientation.PORTRAIT -> Modifier.wrapContentSize()
            LibOrientation.LANDSCAPE -> Modifier
                .wrapContentWidth()
        },
        horizontalAlignment = Alignment.Start
    ) {
        val contentModifier = Modifier
        when (layoutType) {
            LibOrientation.PORTRAIT -> {
                Column(
                    modifier = contentModifier,
                    horizontalAlignment = layoutHorizontalAlignment,
                    content = { layout(deviceOrientation) }
                )
            }
            LibOrientation.LANDSCAPE -> {
                Row(
                    modifier = contentModifier,
                    verticalAlignment = layoutLandscapeVerticalAlignment,
                    content = layoutLandscape!!
                )
            }
            else -> Unit
        }

        buttons?.let { buttons ->
            if (buttonsVisible) {
                Column {
                    buttons.invoke(this, deviceOrientation)
                }
            } else Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}