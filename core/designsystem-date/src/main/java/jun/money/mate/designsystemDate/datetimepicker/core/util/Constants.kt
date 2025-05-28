package jun.money.mate.designsystemDate.datetimepicker.core.util

import java.time.LocalDate

/**
 * Calendar dialog specific constants.
 */
internal object Constants {

    // Default values for CalendarConfig class.

    internal const val DEFAULT_DISPLAY_CALENDAR_WEEKS = false
    internal const val DEFAULT_MONTH_SELECTION = false
    internal const val DEFAULT_YEAR_SELECTION = false

    private val DEFAULT_RANGE_START_DATE = LocalDate.of(1980, 3, 15)
    private const val DEFAULT_RANGE_END_YEAR_OFFSET = 20L
    private val DEFAULT_RANGE_END_DATE = LocalDate.now().plusYears(DEFAULT_RANGE_END_YEAR_OFFSET)
        .withMonth(1)
        .withDayOfMonth(15)

    internal val DEFAULT_RANGE = DEFAULT_RANGE_START_DATE..DEFAULT_RANGE_END_DATE

    // Constants for various indices for better readability

    internal const val RANGE_START = 0
    internal const val RANGE_END = 1
    internal const val FIRST_DAY_IN_MONTH = 1
    internal const val DAYS_IN_WEEK = 7

    // Misc

    internal const val YEAR_GRID_COLUMNS = 1
    internal const val MONTH_GRID_COLUMNS = 4

    internal const val DATE_ITEM_DISABLED_TIMELINE_OPACITY = 0.3f
    internal const val DATE_ITEM_OPACITY = 1f
}
