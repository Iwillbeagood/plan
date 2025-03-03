package jun.money.mate.designsystem_date.datetimepicker.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem_date.datetimepicker.core.util.calcCalendarDateData
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarConfig
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarData
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarDateData
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarSelection
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarViewType
import jun.money.mate.designsystem_date.datetimepicker.models.LibOrientation
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * The view that displays all relevant calendar information.
 * @param dayOfWeekLabels The labels for the days of the week.
 * @param cells The amount of cells / columns that are used for the calendar grid view.
 * @param config The general configuration for the dialog view.
 * @param selection The selection configuration for the dialog.
 * @param data The calculated data of the current calendar view.
 * @param today The date of today.
 * @param onSelect The listener that is invoked when a date is selected.
 * @param selectedDate The date that is currently selected.
 * @param selectedDates The dates that are currently selected.
 * @param selectedRange The range that is currently selected.
 */
internal fun LazyGridScope.setupCalendarSelectionView(
    dayOfWeekLabels: Map<DayOfWeek, String>,
    cells: Int,
    orientation: LibOrientation,
    config: CalendarConfig,
    selection: CalendarSelection,
    data: CalendarData,
    onSelect: (LocalDate) -> Unit,
    selectedDate: LocalDate?,
    selectedDates: List<LocalDate>?,
    selectedRange: Pair<LocalDate?, LocalDate?>,
) {
    val offset = if (config.displayCalendarWeeks) 1 else 0
    items(cells) { cell ->
        val label = dayOfWeekLabels.values.toList().getOrNull(cell - offset)
        label?.let { CalendarHeaderItemComponent(label, TypoTheme.typography.titleLargeB) } ?: CalendarWeekHeaderItemComponent()
    }
    item(span = { GridItemSpan(cells) }) { Spacer(modifier = Modifier.height(4.dp)) }

    data.days.forEach { weekDays ->
        items(weekDays) { day ->
            when (day.first) {
                CalendarViewType.CW -> CalendarWeekItemComponent(
                    value = day.second as String,
                    selection = selection
                )

                CalendarViewType.DAY_START_OFFSET -> CalendarDateItemComponent(
                    data = CalendarDateData(otherMonth = true),
                    orientation = orientation
                )

                CalendarViewType.DAY -> {
                    val dateData = calcCalendarDateData(
                        date = day.second as LocalDate,
                        calendarViewData = data,
                        selection = selection,
                        config = config,
                        selectedDate = selectedDate,
                        selectedDates = selectedDates,
                        selectedRange = selectedRange
                    )
                    dateData?.let {
                        CalendarDateItemComponent(
                            orientation = orientation,
                            data = dateData,
                            onDateClick = onSelect
                        )
                    }
                }
            }
        }
    }
}
