package jun.money.mate.designsystem_date.datetimepicker

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarConfig
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarSelection
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarStyle
import jun.money.mate.designsystem_date.datetimepicker.views.CalendarBottomSheet
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    sheetState: SheetState = rememberModalBottomSheetState(true),
    selectedDate: LocalDate = LocalDate.now(),
    timeBoundary: ClosedRange<LocalDate> = LocalDate.now().let { now -> now..now.plusYears(1) },
    onDismissRequest: () -> Unit,
    onDateSelect: (LocalDate) -> Unit,
) {
    CalendarBottomSheet(
        sheetState = sheetState,
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH,
            boundary = timeBoundary
        ),
        selection = CalendarSelection.Date(
            selectedDate = selectedDate
        ) { newDates ->
            onDateSelect(newDates)
        },
        onDisMissRequest = onDismissRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HmDatePickerPreview() {
    JunTheme {
        DatePicker(
            sheetState = SheetState(true, Density(0f), SheetValue.Expanded, { true }, false),
            onDateSelect = {},
            onDismissRequest = {},
        )
    }
}