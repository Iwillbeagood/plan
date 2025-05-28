package jun.money.mate.designsystemDate.datetimepicker.views

import androidx.compose.foundation.lazy.LazyListScope
import java.time.LocalDate

/**
 * The view that displays all relevant year information.
 * @param yearsRange The range of years
 * @param selectedYear The year that is currently selected.
 * @param onYearClick The listener that is invoked when a year is selected.
 */
internal fun LazyListScope.setupYearSelectionView(
    yearsRange: ClosedRange<Int>,
    selectedYear: Int,
    onYearClick: (Int) -> Unit,
) {
    items(yearsRange.endInclusive.minus(yearsRange.start).plus(1)) {
        val year = yearsRange.start + it
        val selected = selectedYear == year
        val thisYear = year == LocalDate.now().year
        YearItemComponent(
            year = year,
            thisYear = thisYear,
            selected = selected,
            onYearClick = onYearClick,
        )
    }
}
