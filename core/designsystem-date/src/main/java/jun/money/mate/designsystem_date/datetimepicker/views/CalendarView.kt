package jun.money.mate.designsystem_date.datetimepicker.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kic.owner2.designsystem_date.datetimepicker.core.base.BaseBehaviors
import kic.owner2.designsystem_date.datetimepicker.core.base.FrameBase
import kic.owner2.designsystem_date.datetimepicker.core.util.endValue
import kic.owner2.designsystem_date.datetimepicker.core.util.getOrderedDayOfWeekLabels
import kic.owner2.designsystem_date.datetimepicker.core.util.startValue
import kic.owner2.designsystem_date.datetimepicker.models.CalendarConfig
import kic.owner2.designsystem_date.datetimepicker.models.CalendarDisplayMode
import kic.owner2.designsystem_date.datetimepicker.models.CalendarSelection
import kic.owner2.designsystem_date.datetimepicker.models.CalendarStyle
import kic.owner2.designsystem_date.datetimepicker.models.LibOrientation
import kic.owner2.designsystem_date.datetimepicker.models.rememberCalendarState
import java.time.LocalDate

@ExperimentalMaterial3Api
@Composable
internal fun CalendarView(
    selection: CalendarSelection,
    config: CalendarConfig = CalendarConfig(),
    onDisMissRequest: () -> Unit,
) {
    val calendarState = rememberCalendarState(selection, config)

    val coroutine = rememberCoroutineScope()
    val onSelection: (LocalDate) -> Unit = {
        calendarState.processSelection(it)
        BaseBehaviors.autoFinish(
            selection = selection,
            coroutine = coroutine,
            onSelection = calendarState::onFinish,
            onFinished = onDisMissRequest,
            onDisableInput = calendarState::disableInput
        )
    }

    val yearListState = rememberLazyListState()
    LaunchedEffect(calendarState.mode) {
        if (calendarState.mode == CalendarDisplayMode.YEAR) {
            yearListState.scrollToItem(calendarState.yearIndex)
        }
    }

    val weekdays = remember { getOrderedDayOfWeekLabels(config.locale) }

    val density = LocalDensity.current
    FrameBase(
        config = config,
        layoutHorizontalAlignment = Alignment.CenterHorizontally,
        layout = {
            CalendarTopComponent(
                modifier = Modifier.fillMaxWidth(),
                config = config,
                mode = calendarState.mode,
                navigationDisabled = calendarState.mode != CalendarDisplayMode.CALENDAR,
                prevDisabled = calendarState.isPrevDisabled,
                nextDisabled = calendarState.isNextDisabled,
                cameraDate = calendarState.cameraDate,
                onPrev = calendarState::onPrevious,
                onNext = calendarState::onNext,
                monthSelectionEnabled = calendarState.isMonthSelectionEnabled,
                onMonthClick = calendarState::onMonthSelectionClick,
                yearSelectionEnabled = calendarState.isYearSelectionEnabled,
                onYearClick = calendarState::onYearSelectionClick,
            )
            CalendarBaseSelectionComponent(
                modifier = Modifier.wrapContentHeight(),
                orientation = LibOrientation.PORTRAIT,
                yearListState = yearListState,
                mode = calendarState.mode,
                cells = calendarState.cells,
                onCalendarView = {
                    setupCalendarSelectionView(
                        dayOfWeekLabels = weekdays,
                        cells = calendarState.cells,
                        orientation = LibOrientation.PORTRAIT,
                        config = config,
                        selection = selection,
                        data = calendarState.calendarData,
                        onSelect = onSelection,
                        selectedDate = calendarState.date.value,
                        selectedDates = calendarState.dates,
                        selectedRange = Pair(
                            calendarState.range.startValue,
                            calendarState.range.endValue
                        ),
                    )
                },
                onMonthView = {
                    setupMonthSelectionView(
                        monthsData = calendarState.monthsData,
                        onMonthClick = calendarState::onMonthClick
                    )
                },
                onYearView = {
                    setupYearSelectionView(
                        yearsRange = calendarState.yearsRange,
                        selectedYear = calendarState.cameraDate.year,
                        onYearClick = calendarState::onYearClick
                    )
                }
            )
        },
        layoutLandscapeVerticalAlignment = Alignment.Top,
        layoutLandscape = when (config.style) {
            CalendarStyle.MONTH -> {
                {
                    var calendarHeight by remember { mutableIntStateOf(0) }
                    CalendarTopLandscapeComponent(
                        modifier = Modifier
                            .weight(0.3f)
                            .height(density.run { calendarHeight.toDp() }),
                        config = config,
                        mode = calendarState.mode,
                        navigationDisabled = calendarState.mode != CalendarDisplayMode.CALENDAR,
                        prevDisabled = calendarState.isPrevDisabled,
                        nextDisabled = calendarState.isNextDisabled,
                        cameraDate = calendarState.cameraDate,
                        onPrev = calendarState::onPrevious,
                        onNext = calendarState::onNext,
                        onMonthClick = { calendarState.onMonthSelectionClick() },
                        onYearClick = { calendarState.onYearSelectionClick() },
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    CalendarBaseSelectionComponent(
                        modifier = Modifier
                            .weight(0.7f)
                            .onGloballyPositioned { coordinates ->
                                if (calendarHeight != coordinates.size.height)
                                    calendarHeight = coordinates.size.height
                            },
                        orientation = LibOrientation.LANDSCAPE,
                        yearListState = yearListState,
                        cells = calendarState.cells,
                        mode = calendarState.mode,
                        onCalendarView = {
                            setupCalendarSelectionView(
                                dayOfWeekLabels = weekdays,
                                cells = calendarState.cells,
                                orientation = LibOrientation.LANDSCAPE,
                                config = config,
                                selection = selection,
                                data = calendarState.calendarData,
                                onSelect = onSelection,
                                selectedDate = calendarState.date.value,
                                selectedDates = calendarState.dates,
                                selectedRange = Pair(
                                    calendarState.range.startValue,
                                    calendarState.range.endValue
                                ),
                            )
                        },
                        onMonthView = {
                            setupMonthSelectionView(
                                monthsData = calendarState.monthsData,
                                onMonthClick = calendarState::onMonthClick
                            )
                        },
                        onYearView = {
                            setupYearSelectionView(
                                yearsRange = calendarState.yearsRange,
                                selectedYear = calendarState.cameraDate.year,
                                onYearClick = calendarState::onYearClick
                            )
                        },
                    )
                }
            }

            CalendarStyle.WEEK -> null
        },
        buttonsVisible = selection.withButtonView && calendarState.mode == CalendarDisplayMode.CALENDAR
    ) {
        ButtonsComponent(
            onPositiveValid = calendarState.valid,
            onNegative = { selection.onNegativeClick?.invoke() },
            onPositive = calendarState::onFinish,
            onDisMissRequest = onDisMissRequest,
        )
    }
}