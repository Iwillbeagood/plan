package jun.money.mate.designsystem_date.datetimepicker.models

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