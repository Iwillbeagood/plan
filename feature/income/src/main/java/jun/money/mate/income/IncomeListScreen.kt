package jun.money.mate.income

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.BottomToTopSlideFadeAnimatedVisibility
import jun.money.mate.designsystem.component.CrossfadeIfStateChanged
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.ProgressIndicator
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.etc.EmptyMessage
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem_date.datetimepicker.YearMonthPickerScaffold
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarConfig
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarSelection
import jun.money.mate.designsystem_date.datetimepicker.models.CalendarStyle
import jun.money.mate.income.component.IncomeEditButton
import jun.money.mate.income.component.IncomeListBody
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.Income
import java.time.LocalDate

@Composable
internal fun IncomeListRoute(
    onGoBack: () -> Unit,
    onShowIncomeAdd: () -> Unit,
    onShowIncomeEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: IncomeListViewModel = hiltViewModel()
) {
    val incomeListState by viewModel.incomeListState.collectAsStateWithLifecycle()
    val incomeListViewMode by viewModel.incomeListViewMode.collectAsStateWithLifecycle()
    val selectedDate by viewModel.dateState.collectAsStateWithLifecycle()

    IncomeListScreen(
        incomeListState = incomeListState,
        incomeListViewMode = incomeListViewMode,
        selectedDate = selectedDate,
        onGoBack = onGoBack,
        onIncomeClick = viewModel::changeIncomeSelected,
        onIncomeAdd = onShowIncomeAdd,
        onIncomeEdit = viewModel::editIncome,
        onIncomeDelete = viewModel::deleteIncome,
        onDateSelect = viewModel::onDateSelected
    )

    LaunchedEffect(Unit) {
        viewModel.incomeListEffect.collect { effect ->
            when (effect) {
                is IncomeListEffect.EditIncome -> onShowIncomeEdit(effect.id)
                is IncomeListEffect.ShowSnackBar -> onShowSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun IncomeListScreen(
    incomeListState: IncomeListState,
    incomeListViewMode: IncomeListViewMode,
    selectedDate: LocalDate,
    onGoBack: () -> Unit,
    onIncomeClick: (Income) -> Unit,
    onIncomeAdd: () -> Unit,
    onIncomeEdit: () -> Unit,
    onIncomeDelete: () -> Unit,
    onDateSelect: (LocalDate) -> Unit,
) {
    YearMonthPickerScaffold(
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH,
            boundary = LocalDate.now().let { now ->
                now.minusYears(1).withMonth(1).withDayOfMonth(1)..now
            }
        ),
        selection = CalendarSelection.Date(
            selectedDate = selectedDate,
            onSelectDate = onDateSelect
        ),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = tween(durationMillis = 500))
            ) {
                TopAppbar(
                    title = "수입 내역",
                    onBackEvent = onGoBack,
                )
            }
        },
        bottomBar = {
            BottomToTopSlideFadeAnimatedVisibility(
                visible = incomeListViewMode == IncomeListViewMode.EDIT,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    IncomeEditButton(
                        icon = Icons.Default.EditNote,
                        text = "수정",
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onIncomeEdit)
                            .padding(10.dp)
                    )
                    IncomeEditButton(
                        icon = Icons.Default.DeleteOutline,
                        text = "삭제",
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onIncomeDelete)
                            .padding(5.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            FadeAnimatedVisibility(
                visible = incomeListViewMode == IncomeListViewMode.LIST,
            ) {
                Button(
                    modifier= Modifier.size(56.dp),
                    shape = CircleShape,
                    elevation = ButtonDefaults.buttonElevation(8.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = onIncomeAdd
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = White1,
                        contentDescription = "수입 추가",
                        modifier = Modifier
                            .size(35.dp)
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
        ) {
            IncomeListContent(
                incomeListState = incomeListState,
                onIncomeClick = onIncomeClick,
            )
        }
    }
}

@Composable
private fun IncomeListContent(
    incomeListState: IncomeListState,
    onIncomeClick: (Income) -> Unit,
) {
    CrossfadeIfStateChanged(incomeListState) {
        when (it) {
            is IncomeListState.Loading -> ProgressIndicator()
            IncomeListState.Empty -> EmptyMessage("수입 내역이 없습니다.")
            is IncomeListState.IncomeListData -> {
                IncomeListBody(
                    incomeList = it.incomeList,
                    onIncomeClick = onIncomeClick
                )
            }
        }
    }
}

@Preview
@Composable
private fun IncomeListScreenPreview() {
    JunTheme {
        IncomeListScreen(
            incomeListState = IncomeListState.Loading,
            selectedDate = LocalDate.now(),
            onGoBack = {},
            onDateSelect = {},
            onIncomeAdd = {},
            onIncomeClick = {},
            onIncomeEdit = {},
            onIncomeDelete = {},
            incomeListViewMode = IncomeListViewMode.EDIT
        )
    }
}