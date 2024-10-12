package jun.money.mate.designsystem_date.datetimepicker.models

import jun.money.mate.designsystem_date.datetimepicker.core.util.BaseConstants
import jun.money.mate.designsystem_date.datetimepicker.core.util.Constants
import java.time.LocalDate
import java.util.Locale

class CalendarConfig(
    val locale: Locale = Locale.getDefault(),
    val style: CalendarStyle = CalendarStyle.MONTH,
    val cameraDate: LocalDate? = null,
    val displayCalendarWeeks: Boolean = Constants.DEFAULT_DISPLAY_CALENDAR_WEEKS,
    val monthSelection: Boolean = Constants.DEFAULT_MONTH_SELECTION,
    val yearSelection: Boolean = Constants.DEFAULT_YEAR_SELECTION,
    val boundary: ClosedRange<LocalDate> = Constants.DEFAULT_RANGE,
    val disabledDates: List<LocalDate>? = null,
) : BaseConfigs()

abstract class BaseConfigs(
    open val orientation: LibOrientation? = BaseConstants.DEFAULT_LIB_LAYOUT,
)

enum class LibOrientation {
    PORTRAIT,
    LANDSCAPE,
}