package jun.money.mate.designsystem_date.datetimepicker.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem.theme.main
import jun.money.mate.designsystem_date.datetimepicker.core.util.Constants
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarMonthData
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

/**
 * The view that displays all relevant year information.
 * @param monthsData The information for the months selection.
 * @param onMonthClick The listener that is invoked when a month is selected.
 */
internal fun LazyGridScope.setupMonthSelectionView(
    monthsData: CalendarMonthData,
    onMonthClick: (Month) -> Unit,
) {
    items(Month.entries.toTypedArray()) { month ->
        val selected = monthsData.selected == month
        val disabled = monthsData.disabled.contains(month)
        val thisMonth = monthsData.thisMonth == month
        MonthItemComponent(
            month = month,
            selected = selected,
            disabled = disabled,
            thisMonth = thisMonth,
            onMonthClick = { onMonthClick(month) }
        )
    }
}

/**
 * The item component of the month selection view.
 * @param month The month that this item represents.
 * @param thisMonth The current month.
 * @param selected If the month is selected.
 * @param onMonthClick The listener that is invoked when a year is selected.
 */
@Composable
internal fun MonthItemComponent(
    month: Month,
    thisMonth: Boolean = false,
    disabled: Boolean = false,
    selected: Boolean = false,
    onMonthClick: () -> Unit
) {
    val textStyle =
        when {
            selected -> JUNTheme.typography.titleLargeM.copy(White1)
            thisMonth -> JUNTheme.typography.titleLargeM.copy(MaterialTheme.colorScheme.primary)
            else -> JUNTheme.typography.titleLargeM
        }

    val baseModifier = Modifier
        .wrapContentWidth()
        .padding(4.dp)
        .clickable(!disabled) { onMonthClick() }

    val normalModifier = baseModifier
        .clip(RoundedCornerShape(5.dp))

    val selectedModifier = normalModifier
        .background(main)

    val textAlpha = when {
        disabled -> Constants.DATE_ITEM_DISABLED_TIMELINE_OPACITY
        else -> Constants.DATE_ITEM_OPACITY
    }

    Column(
        modifier = when {
            disabled -> normalModifier
            selected -> selectedModifier
            thisMonth -> baseModifier
            else -> normalModifier
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .alpha(textAlpha)
                .padding(horizontal = 12.dp)
                .padding(vertical = 8.dp),
            text = LocalDate.now().withMonth(month.value)
                .format(DateTimeFormatter.ofPattern("MMM")),
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}