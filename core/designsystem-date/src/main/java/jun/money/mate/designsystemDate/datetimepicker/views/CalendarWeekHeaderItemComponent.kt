package jun.money.mate.designsystemDate.datetimepicker.views

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable function that represents a calendar week header item with the given [label].
 */
@Composable
internal fun CalendarWeekHeaderItemComponent() {
    Row(
        modifier = Modifier
            .aspectRatio(1f, true)
            .padding(4.dp)
            .clip(MaterialTheme.shapes.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "CW",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
