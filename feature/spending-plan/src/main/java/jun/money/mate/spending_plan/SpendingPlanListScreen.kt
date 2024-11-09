package jun.money.mate.spending_plan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.etc.EmptyMessage
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.spending_plan.component.SpendingPlanListBody
import jun.money.mate.ui.IncomeSpendingScaffold
import java.time.LocalDate

@Composable
internal fun SpendingPlanListRoute(
    onGoBack: () -> Unit,
    onShowSpendingPlanAdd: () -> Unit,
    onShowSpendingPlanEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: SpendingPlanListViewModel = hiltViewModel()
) {
    val spendingPlanListState by viewModel.spendingPlanListState.collectAsStateWithLifecycle()
    val spendingListViewMode by viewModel.spendingListViewMode.collectAsStateWithLifecycle()
    val selectedDate by viewModel.dateState.collectAsStateWithLifecycle()

    SpendingListScreen(
        spendingPlanListState = spendingPlanListState,
        spendingListViewMode = spendingListViewMode,
        selectedDate = selectedDate,
        onGoBack = onGoBack,
        onSpendingPlanClick = viewModel::changeSpendingSelected,
        onSpendingPlanAdd = onShowSpendingPlanAdd,
        onSpendingPlanEdit = viewModel::editSpending,
        onSpendingPlanDelete = viewModel::deleteSpending,
        onDateSelect = viewModel::dateSelected,
        onSpendingTabClick = viewModel::spendingTabClick
    )

    LaunchedEffect(Unit) {
        viewModel.spendingPlanListEffect.collect { effect ->
            when (effect) {
                is SpendingPlanListEffect.EditSpendingPlan -> onShowSpendingPlanEdit(effect.id)
                is SpendingPlanListEffect.ShowSnackBar -> onShowSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun SpendingListScreen(
    spendingPlanListState: SpendingPlanListState,
    spendingListViewMode: SpendingListViewMode,
    selectedDate: LocalDate,
    onGoBack: () -> Unit,
    onSpendingPlanClick: (SpendingPlan) -> Unit,
    onSpendingPlanAdd: () -> Unit,
    onSpendingPlanEdit: () -> Unit,
    onSpendingPlanDelete: () -> Unit,
    onDateSelect: (LocalDate) -> Unit,
    onSpendingTabClick: (Int) -> Unit,
) {
    IncomeSpendingScaffold(
        title = "지출 계획",
        color = Red3,
        bottomBarVisible = spendingListViewMode == SpendingListViewMode.EDIT,
        addButtonVisible = spendingListViewMode == SpendingListViewMode.LIST,
        selectedDate = selectedDate,
        onDateSelect = onDateSelect,
        onAdd = onSpendingPlanAdd,
        onEdit = onSpendingPlanEdit,
        onDelete = onSpendingPlanDelete,
        onGoBack = onGoBack,
    ) {
        SpendingListContent(
            spendingPlanListState = spendingPlanListState,
            onSpendingPlanClick = onSpendingPlanClick,
            onSpendingTabClick = onSpendingTabClick
        )
    }
}

@Composable
private fun SpendingListContent(
    spendingPlanListState: SpendingPlanListState,
    onSpendingPlanClick: (SpendingPlan) -> Unit,
    onSpendingTabClick: (Int) -> Unit,
) {
    FadeAnimatedVisibility(spendingPlanListState is SpendingPlanListState.Empty) {
        if (spendingPlanListState is SpendingPlanListState.Empty) {
            EmptyMessage("지출 계획이 없습니다.")
        }
    }

    FadeAnimatedVisibility(spendingPlanListState is SpendingPlanListState.SpendingPlanListData) {
        if (spendingPlanListState is SpendingPlanListState.SpendingPlanListData) {
            SpendingPlanListBody(
                spendingPlanList = spendingPlanListState.filterSpendingPlanList,
                spendingTypeTabIndex = spendingPlanListState.spendingTypeTabIndex,
                onSpendingPlanClick = onSpendingPlanClick,
                onSpendingTabClick = onSpendingTabClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingListScreenPreview() {
    JunTheme {
        SpendingListScreen(
            spendingPlanListState = SpendingPlanListState.Loading,
            selectedDate = LocalDate.now(),
            onGoBack = {},
            onDateSelect = {},
            onSpendingPlanAdd = {},
            onSpendingPlanClick = {},
            onSpendingPlanEdit = {},
            onSpendingPlanDelete = {},
            onSpendingTabClick = {},
            spendingListViewMode = SpendingListViewMode.EDIT
        )
    }
}