package jun.money.mate.designsystem_date.datetimepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.DefaultBottomSheet
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem_date.datetimepicker.views.Picker
import jun.money.mate.designsystem_date.datetimepicker.views.TimeFormat
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayPickerSheet(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    currentMonth: Int = LocalDate.now().monthValue,
) {
    val today = LocalDate.now()
    val daysInMonth =
        remember(currentMonth) { YearMonth.of(today.year, currentMonth).lengthOfMonth() }
    val dates = (1..daysInMonth).map { "$it" }.toImmutableList()
    var selectedDate by remember { mutableStateOf(today.dayOfMonth.toString()) }

    DefaultBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "날짜 선택",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Picker(
                texts = dates,
                count = daysInMonth,
                rowCount = 5,
                size = DpSize(100.dp, 150.dp),
                startIndex = dates.indexOf(selectedDate),
                onItemSelected = { selectedDate = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onDateSelected("${currentMonth}월 ${selectedDate}일")
                    onDismiss()
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("확인")
            }
        }
    }
}

@Composable
fun DayPicker(
    onDaySelected: (String) -> Unit,
    currentMonth: Int = LocalDate.now().monthValue,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val daysInMonth = remember(currentMonth) { YearMonth.of(today.year, currentMonth).lengthOfMonth() }
    val dates = (1..daysInMonth).map { "$it" }.toImmutableList()

    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, Gray6),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Picker(
                texts = dates,
                count = daysInMonth,
                rowCount = 5,
                size = DpSize(100.dp, 150.dp),
                startIndex = dates.indexOf(LocalDate.now().dayOfMonth.toString()),
                onItemSelected = onDaySelected,
                timeFormat = TimeFormat.DAY,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DayPickerPreview() {
    JunTheme {
        DayPickerSheet(
            sheetState = SheetState(true, Density(0f), SheetValue.Expanded, { true }, false),
            onDateSelected = {},
            onDismiss = {},
            currentMonth = 2
        )
    }
}
