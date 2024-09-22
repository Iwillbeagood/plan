package jun.money.mate.designsystem_date.datetimepicker.core.util

import android.R
import androidx.compose.ui.unit.dp
import kic.owner2.designsystem_date.datetimepicker.models.LibOrientation
import kic.owner2.designsystem_date.datetimepicker.models.ButtonStyle
import kic.owner2.designsystem_date.datetimepicker.views.SelectionButton

object BaseConstants {

    // Behaviours

    const val SUCCESS_DISMISS_DELAY = 300L

    val DEFAULT_LIB_LAYOUT: LibOrientation? = null // Auto orientation

    val KEYBOARD_HEIGHT_MAX = 300.dp
    const val KEYBOARD_RATIO = 0.8f

    val DYNAMIC_SIZE_MAX = 200.dp

    val DEFAULT_NEGATIVE_BUTTON = SelectionButton(
        textRes = R.string.cancel,
        type = ButtonStyle.TEXT
    )

    val DEFAULT_POSITIVE_BUTTON =
        SelectionButton(
            textRes = R.string.ok,
            type = ButtonStyle.TEXT
        )
}