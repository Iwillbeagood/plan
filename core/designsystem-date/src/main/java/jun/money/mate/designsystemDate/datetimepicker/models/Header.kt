package jun.money.mate.designsystemDate.datetimepicker.models

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import jun.money.mate.designsystemDate.datetimepicker.core.util.IconSource

abstract class Header {

    /**
     * Standard implementation of a header.
     * @param title The title of the header.
     * @param icon The icon of the header.
     */
    data class Default(
        val title: String,
        val icon: IconSource? = null,
    ) : Header()

    /**
     * Custom implementation of a header.
     * @param header The custom header implementation with the horizontal padding values of the default header.
     */
    data class Custom(
        val header: @Composable (paddingValues: PaddingValues) -> Unit,
    ) : Header()
}
