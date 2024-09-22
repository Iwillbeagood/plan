package jun.money.mate.designsystem_date.datetimepicker.models

import java.time.Month

/**
 * Defines information for the months selection.
 * @param disabled the months that are displayed.
 * @param selected the month that is selected.
 * @param thisMonth the current month.
 */
internal data class CalendarMonthData(
    val selected: Month,
    val disabled: List<Month>,
    val thisMonth: Month,
)