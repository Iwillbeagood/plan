package jun.money.mate.designsystemDate.datetimepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystemDate.datetimepicker.models.CalendarConfig
import jun.money.mate.designsystemDate.datetimepicker.models.CalendarSelection
import jun.money.mate.designsystemDate.datetimepicker.models.CalendarStyle
import jun.money.mate.designsystemDate.datetimepicker.views.CalendarBottomSheet
import jun.money.mate.designsystemDate.datetimepicker.views.CalendarView
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSheet(
    sheetState: SheetState = rememberModalBottomSheetState(true),
    selectedDate: LocalDate = LocalDate.now(),
    timeBoundary: ClosedRange<LocalDate> = LocalDate.now().let { now -> now.withDayOfMonth(1)..now.withDayOfMonth(now.lengthOfMonth()) },
    onDismissRequest: () -> Unit,
    onDateSelect: (LocalDate) -> Unit,
) {
    CalendarBottomSheet(
        sheetState = sheetState,
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH,
            boundary = timeBoundary,
        ),
        selection = CalendarSelection.Date(
            selectedDate = selectedDate,
        ) { newDates ->
            onDateSelect(newDates)
        },
        onDisMissRequest = onDismissRequest,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    onDateSelect: (LocalDate) -> Unit,
    selectedDate: LocalDate? = null,
    timeBoundary: ClosedRange<LocalDate> = LocalDate.now().let { now -> now.withDayOfMonth(1)..now.withDayOfMonth(now.lengthOfMonth()) },
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, Gray6),
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
        ) {
            CalendarView(
                config = CalendarConfig(
                    yearSelection = true,
                    monthSelection = true,
                    style = CalendarStyle.MONTH,
                    boundary = timeBoundary,
                ),
                onDisMissRequest = { },
                selection = CalendarSelection.Date(
                    withButtonView = false,
                    selectedDate = selectedDate,
                ) { newDates ->
                    onDateSelect(newDates)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DatePickerSheetPreview() {
    JunTheme {
        DatePickerSheet(
            sheetState = SheetState(true, Density(0f), SheetValue.Expanded, { true }, false),
            onDateSelect = {},
            onDismissRequest = {},
        )
    }
}

@Preview
@Composable
private fun DatePickerPreview() {
    JunTheme {
        DatePicker(
            onDateSelect = {},
        )
    }
}
