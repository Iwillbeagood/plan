package jun.money.mate.designsystem_date.datetimepicker.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Composable function that represents a calendar header item with the given [label].
 *
 * @param label The label to display in the header item.
 */
@Composable
internal fun CalendarHeaderItemComponent(
    label: String,
    headerTextStyle: TextStyle
) {
    Row(
        modifier = Modifier
            .aspectRatio(1f, true)
            .padding(4.dp)
            .clip(MaterialTheme.shapes.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = headerTextStyle,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}