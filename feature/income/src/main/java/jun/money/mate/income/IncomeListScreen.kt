package jun.money.mate.income

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.etc.EmptyMessage
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.main
import jun.money.mate.income.component.IncomeListBody
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.Income
import jun.money.mate.ui.DefaultScaffold
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
    DefaultScaffold(
        title = "수입 내역",
        color = main,
        bottomBarVisible = incomeListViewMode == IncomeListViewMode.EDIT,
        addButtonVisible = incomeListViewMode == IncomeListViewMode.LIST,
        selectedDate = selectedDate,
        onDateSelect = onDateSelect,
        onAdd = onIncomeAdd,
        onEdit = onIncomeEdit,
        onDelete = onIncomeDelete,
        onGoBack = onGoBack,
    ) {
        IncomeListContent(
            incomeListState = incomeListState,
            onIncomeClick = onIncomeClick,
        )
    }
}

@Composable
private fun IncomeListContent(
    incomeListState: IncomeListState,
    onIncomeClick: (Income) -> Unit,
) {
    FadeAnimatedVisibility(incomeListState is IncomeListState.Empty) {
        if (incomeListState is IncomeListState.Empty) {
            EmptyMessage("수입 내역이 없습니다.")
        }
    }
    FadeAnimatedVisibility(incomeListState is IncomeListState.IncomeListData) {
        if (incomeListState is IncomeListState.IncomeListData) {
            IncomeListBody(
                incomeList = incomeListState.incomeList,
                onIncomeClick = onIncomeClick
            )
        }
    }
}

@Preview(showBackground = true)
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