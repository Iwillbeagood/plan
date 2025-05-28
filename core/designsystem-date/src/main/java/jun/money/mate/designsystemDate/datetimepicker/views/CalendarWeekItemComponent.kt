package jun.money.mate.designsystemDate.datetimepicker.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jun.money.mate.designsystem.theme.nonScaledSp
import jun.money.mate.designsystemDate.datetimepicker.models.CalendarSelection

/**
 * The calendar week item component of the calendar view.
 * @param value The value of the week.
 * @param selection The selection configuration for the dialog view.
 */
@Composable
internal fun CalendarWeekItemComponent(
    value: String,
    selection: CalendarSelection,
) {
    val normalModifier = Modifier.aspectRatio(1f, true)
    val textStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
    val parentModifier = when (selection) {
        is CalendarSelection.Date -> Modifier.padding(2.dp)
        is CalendarSelection.Dates -> Modifier.padding(2.dp)
        is CalendarSelection.Period -> Modifier.padding(vertical = 2.dp)
    }
    Column(modifier = parentModifier) {
        Row(
            modifier = normalModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier,
                text = value,
                style = textStyle.nonScaledSp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
