package jun.money.mate.designsystemDate.datetimepicker.views

import androidx.annotation.StringRes
import androidx.compose.ui.text.AnnotatedString
import jun.money.mate.designsystemDate.datetimepicker.core.util.IconSource
import jun.money.mate.designsystemDate.datetimepicker.models.ButtonStyle

open class SelectionButton {

    internal val text: String?

    @StringRes
    internal val textRes: Int?
    val annotatedString: AnnotatedString?
    internal val icon: IconSource?
    internal val type: ButtonStyle

    /**
     * Creates a new instance of `SelectionButton` with the given text, icon and type.
     *
     * @param text The text to be displayed on the button.
     * @param icon The icon to be displayed on the button. Can be `null`.
     * @param type The style of the button. Default value is `ButtonStyle.TEXT`.
     */
    constructor(
        text: String,
        icon: IconSource? = null,
        type: ButtonStyle = ButtonStyle.TEXT,
    ) {
        this.text = text
        this.textRes = null
        this.annotatedString = null
        this.icon = icon
        this.type = type
    }

    /**
     * Creates a new instance of `SelectionButton` with the given string resource identifier, icon and type.
     *
     * @param textRes The string resource identifier to be displayed on the button.
     * @param icon The icon to be displayed on the button. Can be `null`.
     * @param type The style of the button. Default value is `ButtonStyle.TEXT`.
     */
    constructor(
        @StringRes textRes: Int,
        icon: IconSource? = null,
        type: ButtonStyle = ButtonStyle.TEXT,
    ) {
        this.textRes = textRes
        this.text = null
        this.annotatedString = null
        this.icon = icon
        this.type = type
    }

    /**
     * Creates a new instance of `SelectionButton` with the given `AnnotatedString`, icon and type.
     *
     * @param annotatedString The annotated string to be displayed on the button.
     * @param icon The icon to be displayed on the button. Can be `null`.
     * @param type The style of the button. Default value is `ButtonStyle.TEXT`.
     */
    constructor(
        annotatedString: AnnotatedString,
        icon: IconSource? = null,
        type: ButtonStyle = ButtonStyle.TEXT,
    ) {
        this.annotatedString = annotatedString
        this.textRes = null
        this.text = null
        this.icon = icon
        this.type = type
    }
}
