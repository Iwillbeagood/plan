package jun.money.mate.designsystem_date.datetimepicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.DefaultBottomSheet
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.LightBlue2
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarConfig
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarDisplayMode
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarSelection
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarState
import jun.money.mate.designsystem_date.datetimepicker.models.rememberCalendarState
import jun.money.mate.designsystem_date.datetimepicker.views.CalendarTopComponent
import jun.money.mate.res.R

@Composable
fun YearMonthPickerScaffold(
    selection: CalendarSelection,
    config: CalendarConfig,
    topBar: @Composable (() -> Unit),
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val calendarState = rememberCalendarState(selection, config)
    val onSelection: () -> Unit = {
        calendarState.processSelection(calendarState.cameraDate)
        calendarState.disableInput()
        calendarState.onFinish()
    }

    YearMonthPickerScaffold(
        calendarState = calendarState,
        config = config,
        onSelection = onSelection,
        onDismissRequest = calendarState::onResModeToCALENDAR,
        floatingActionButton = floatingActionButton,
        topBar = topBar,
        bottomBar = bottomBar,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearMonthPickerScaffold(
    calendarState: CalendarState,
    config: CalendarConfig,
    onSelection: () -> Unit,
    onDismissRequest: () -> Unit,
    topBar: @Composable (() -> Unit),
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column {
                CalendarTopComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    config = config,
                    mode = calendarState.mode,
                    navigationDisabled = calendarState.mode != CalendarDisplayMode.CALENDAR,
                    prevDisabled = calendarState.isPrevDisabled,
                    nextDisabled = calendarState.isNextDisabled,
                    cameraDate = calendarState.cameraDate,
                    onPrev = {
                        calendarState.onPrevious()
                        onSelection()
                    },
                    onNext = {
                        calendarState.onNext()
                        onSelection()
                    },
                    monthSelectionEnabled = calendarState.isMonthSelectionEnabled,
                    onMonthClick = calendarState::onMonthSelectionClick,
                    yearSelectionEnabled = calendarState.isYearSelectionEnabled,
                    onYearClick = calendarState::onYearSelectionClick,
                )
                HorizontalDivider(
                    lineColor = LightBlue2)
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
            }
        }
    }

    if (calendarState.mode != CalendarDisplayMode.CALENDAR) {
        DefaultBottomSheet(
            sheetContent = {
                Column {
                    YearMonthPickerBottom(
                        calendarState = calendarState,
                        onSelection = onSelection
                    )
                    VerticalSpacer(20.dp)
                }
            },
            sheetContentSpace = 10.dp,
            sheetButton1 = {
                RegularButton (
                    text = stringResource(id = R.string.btn_close),
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f)
                )
            },
            onDismissRequest = onDismissRequest
        )
    }

}