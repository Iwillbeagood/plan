package jun.money.mate.designsystemDate.datetimepicker

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import jun.money.mate.designsystem.component.DefaultBottomSheet
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystemDate.datetimepicker.views.TimePicker
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownTimePicker(
    sheetState: SheetState = rememberModalBottomSheetState(true),
    selectedTime: LocalTime = LocalTime.now(),
    onDismissRequest: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
) {
    var snappedTime by remember { mutableStateOf(selectedTime) }

    DefaultBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        sheetContent = {
            TimePicker(
                startTime = snappedTime,
            ) { newTime ->
                snappedTime = newTime
            }
        },
        sheetButton1 = {
            RegularButton(
                text = "취소",
                modifier = Modifier.weight(3f),
                isActive = false,
                onClick = onDismissRequest,
            )
        },
        sheetButton2 = {
            RegularButton(
                text = "확인",
                modifier = Modifier.weight(7f),
                onClick = {
                    onDismissRequest()
                    onTimeSelected(
                        snappedTime,
                    )
                },
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HTimePickerPreview() {
    JunTheme {
        DropDownTimePicker(
            sheetState = SheetState(true, Density(0f), SheetValue.Expanded, { true }, false),
            onDismissRequest = {},
        ) {}
    }
}
