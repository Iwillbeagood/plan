package jun.money.mate.designsystem_date.datetimepicker.models

import android.util.Range
import jun.money.mate.designsystem_date.datetimepicker.core.util.BaseConstants
import jun.money.mate.designsystem_date.datetimepicker.views.SelectionButton
import java.time.LocalDate

/**
 * The selection configuration for the calendar dialog.
 */
sealed class CalendarSelection : BaseSelection() {

    /**
     * Select a date.
     * @param withButtonView Show the dialog with the buttons view.
     * @param extraButton An extra button that can be used for a custom action.
     * @param onExtraButtonClick The listener that is invoked when the extra button is clicked.
     * @param negativeButton The button that will be used as a negative button.
     * @param onNegativeClick The listener that is invoked when the negative button is clicked.
     * @param positiveButton The button that will be used as a positive button.
     * @param selectedDate The date that is selected by default.
     * @param onSelectDate The listener that returns the selected date.
     */
    class Date(
        override val withButtonView: Boolean = true,
        override val extraButton: SelectionButton? = null,
        override val onExtraButtonClick: (() -> Unit)? = null,
        override val negativeButton: SelectionButton? = BaseConstants.DEFAULT_NEGATIVE_BUTTON,
        override val onNegativeClick: (() -> Unit)? = null,
        override val positiveButton: SelectionButton = BaseConstants.DEFAULT_POSITIVE_BUTTON,
        val selectedDate: LocalDate? = null,
        val onSelectDate: (date: LocalDate) -> Unit
    ) : CalendarSelection()

    /**
     * Select multiple dates.
     * @param extraButton An extra button that can be used for a custom action.
     * @param onExtraButtonClick The listener that is invoked when the extra button is clicked.
     * @param negativeButton The button that will be used as a negative button.
     * @param onNegativeClick The listener that is invoked when the negative button is clicked.
     * @param positiveButton The button that will be used as a positive button.
     * @param selectedDates A list of dates that is selected by default.
     * @param onSelectDates The listener that returns the selected dates.
     */
    class Dates(
        override val extraButton: SelectionButton? = null,
        override val onExtraButtonClick: (() -> Unit)? = null,
        override val negativeButton: SelectionButton? = BaseConstants.DEFAULT_NEGATIVE_BUTTON,
        override val onNegativeClick: (() -> Unit)? = null,
        override val positiveButton: SelectionButton = BaseConstants.DEFAULT_POSITIVE_BUTTON,
        val selectedDates: List<LocalDate>? = null,
        val onSelectDates: (dates: List<LocalDate>) -> Unit
    ) : CalendarSelection()

    /**
     * Select a range (start and end date).
     * @param withButtonView Show the dialog with the buttons view.
     * @param extraButton An extra button that can be used for a custom action.
     * @param onExtraButtonClick The listener that is invoked when the extra button is clicked.
     * @param negativeButton The button that will be used as a negative button.
     * @param onNegativeClick The listener that is invoked when the negative button is clicked.
     * @param positiveButton The button that will be used as a positive button.
     * @param selectedRange The range that is selected by default.
     * @param onSelectRange The listener that returns the selected range.
     */
    class Period(
        override val withButtonView: Boolean = true,
        override val extraButton: SelectionButton? = null,
        override val onExtraButtonClick: (() -> Unit)? = null,
        override val negativeButton: SelectionButton? = BaseConstants.DEFAULT_NEGATIVE_BUTTON,
        override val onNegativeClick: (() -> Unit)? = null,
        override val positiveButton: SelectionButton = BaseConstants.DEFAULT_POSITIVE_BUTTON,
        val selectedRange: Range<LocalDate>? = null,
        val onSelectRange: (startDate: LocalDate, endDate: LocalDate) -> Unit
    ) : CalendarSelection()
}