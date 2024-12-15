package jun.money.mate.designsystem_date.datetimepicker.views

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import jun.money.mate.designsystem_date.datetimepicker.core.base.BottomSheetBase
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarConfig
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarSelection

@ExperimentalMaterial3Api
@Composable
internal fun CalendarBottomSheet(
    sheetState: SheetState,
    config: CalendarConfig = CalendarConfig(),
    properties: DialogProperties = DialogProperties(),
    selection: CalendarSelection,
    onDisMissRequest: () -> Unit,
) {
    BottomSheetBase(
        sheetState = sheetState,
        properties = properties,
        onDismissRequest = onDisMissRequest,
    ) {
        CalendarView(
            config = config,
            onDisMissRequest = onDisMissRequest,
            selection = selection,
        )
    }
}