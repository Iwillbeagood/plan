package jun.money.mate.designsystem_date.datetimepicker

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.theme.LightBlue2
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarConfig
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarDisplayMode
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarSelection
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarState
import jun.money.mate.designsystem_date.datetimepicker.models.rememberCalendarState
import jun.money.mate.designsystem_date.datetimepicker.views.CalendarTopComponent
import jun.money.mate.designsystem_date.datetimepicker.views.setupMonthSelectionView
import jun.money.mate.designsystem_date.datetimepicker.views.setupYearSelectionView
import jun.money.mate.res.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearMonthPickerScaffold(
    scaffoldState: BottomSheetScaffoldState,
    selection: CalendarSelection,
    config: CalendarConfig,
    topBar: @Composable (() -> Unit),
    content: @Composable (PaddingValues) -> Unit
) {
    val calendarState = rememberCalendarState(selection, config)
    val coroutine = rememberCoroutineScope()
    val onSelection: () -> Unit = {
        calendarState.processSelection(calendarState.cameraDate)
        calendarState.disableInput()
        calendarState.onFinish()
        coroutine.launch {
            scaffoldState.bottomSheetState.hide()
        }
    }

    BottomSheetScaffold(
        sheetContainerColor = MaterialTheme.colorScheme.surfaceDim,
        containerColor = MaterialTheme.colorScheme.surfaceDim,
        scaffoldState = scaffoldState,
        sheetDragHandle = null,
        sheetShadowElevation = 10.dp,
        sheetContent = {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                YearMonthPickerBottom(
                    scaffoldState = scaffoldState,
                    calendarState = calendarState,
                    onSelection = onSelection
                )
                Spacer(modifier = Modifier.height(20.dp))
                RegularButton(
                    text = stringResource(id = R.string.btn_close),
                    onClick = {
                        coroutine.launch {
                            calendarState.onResModeToCALENDAR()
                            scaffoldState.bottomSheetState.hide()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        },
        topBar = topBar
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                YearMonthPickerTop(
                    scaffoldState = scaffoldState,
                    calendarState = calendarState,
                    onSelection = onSelection,
                    config = config
                )
                HorizontalDivider(lineColor = LightBlue2)
                Box(modifier = Modifier.weight(1f)) {
                    content(padding)
                }
            }
            Scrim(
                color = Color.Gray,
                visible = scaffoldState.bottomSheetState.isVisible
            )
        }
    }
}

@Composable
private fun Scrim(
    color: Color,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 0.5f else 0f,
            animationSpec = TweenSpec(), label = ""
        )
        val dismissSheet = if (visible) {
            Modifier
                .clearAndSetSemantics {}
                .clickable(enabled = false) {}
        } else {
            Modifier
        }
        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissSheet)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearMonthPickerTop(
    scaffoldState: BottomSheetScaffoldState,
    calendarState: CalendarState,
    onSelection: () -> Unit,
    config: CalendarConfig = CalendarConfig()
) {
    val coroutine = rememberCoroutineScope()

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
        onMonthClick = {
            calendarState.onMonthSelectionClick()
            coroutine.launch {
                scaffoldState.bottomSheetState.show()
            }
        },
        yearSelectionEnabled = calendarState.isYearSelectionEnabled,
        onYearClick = {
            calendarState.onYearSelectionClick()
            coroutine.launch {
                scaffoldState.bottomSheetState.show()
            }
        },
    )
}

@OptIn(ExperimentalSnapperApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun YearMonthPickerBottom(
    scaffoldState: BottomSheetScaffoldState,
    calendarState: CalendarState,
    onSelection: () -> Unit
) {
    val coroutine = rememberCoroutineScope()


    val yearListState = rememberLazyListState()
    LaunchedEffect(calendarState.mode) {
        if (calendarState.mode == CalendarDisplayMode.YEAR) {
            yearListState.scrollToItem(calendarState.yearIndex)
        }
    }

    val baseViewModifier = Modifier
        .padding(top = 16.dp)

    val gridYearModifier = baseViewModifier
        .graphicsLayer { alpha = 0.99F }
        .drawWithContent {
            drawContent()
        }

    val behavior = rememberSnapperFlingBehavior(
        lazyListState = yearListState,
        snapOffsetForItem = SnapOffsets.Center,
    )

    when (calendarState.mode) {
        CalendarDisplayMode.MONTH -> {
            Column(
                modifier = Modifier.wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyVerticalGrid(
                    modifier = baseViewModifier,
                    columns = GridCells.Fixed(calendarState.cells),
                    content = {
                        setupMonthSelectionView(
                            monthsData = calendarState.monthsData,
                            onMonthClick = {
                                coroutine.launch {
                                    calendarState.onMonthClick(it)
                                    onSelection()
                                    scaffoldState.bottomSheetState.hide()
                                }
                            }
                        )
                    }
                )
            }
        }

        CalendarDisplayMode.YEAR -> {
            Column(
                modifier = Modifier.wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyRow(
                    modifier = gridYearModifier,
                    state = yearListState,
                    flingBehavior = behavior,
                    contentPadding = PaddingValues(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        setupYearSelectionView(
                            yearsRange = calendarState.yearsRange,
                            selectedYear = calendarState.cameraDate.year,
                            onYearClick = {
                                coroutine.launch {
                                    calendarState.onYearClick(it)
                                    onSelection()
                                    scaffoldState.bottomSheetState.hide()
                                }
                            }
                        )
                    }
                )
            }
        }

        else -> {}
    }
}