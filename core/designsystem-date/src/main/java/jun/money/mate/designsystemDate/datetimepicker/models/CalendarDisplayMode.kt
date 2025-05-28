package jun.money.mate.designsystemDate.datetimepicker.models

internal enum class CalendarDisplayMode {

    /**
     * The default calendar view.
     */
    CALENDAR,

    /**
     * The month selection view, if [CalendarConfig.monthSelection] is enabled.
     */
    MONTH,

    /**
     * The year selection view, if [CalendarConfig.yearSelection] is enabled.
     */
    YEAR,
}
