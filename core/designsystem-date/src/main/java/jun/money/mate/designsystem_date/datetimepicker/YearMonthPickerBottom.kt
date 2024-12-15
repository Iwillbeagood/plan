package jun.money.mate.designsystem_date.datetimepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarDisplayMode
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarState
import jun.money.mate.designsystem_date.datetimepicker.views.setupMonthSelectionView
import jun.money.mate.designsystem_date.datetimepicker.views.setupYearSelectionView
import kotlinx.coroutines.launch

@OptIn(ExperimentalSnapperApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun YearMonthPickerBottom(
    calendarState: CalendarState,
    onSelection: () -> Unit = {},
    sheetState: SheetState = rememberModalBottomSheetState(true)
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
                                    sheetState.hide()
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
                                    sheetState.hide()
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