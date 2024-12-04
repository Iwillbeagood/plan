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
import jun.money.mate.model.etc.ViewMode
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.spending_plan.component.SpendingPlanListBody
import jun.money.mate.ui.DefaultScaffold

@Composable
internal fun SpendingPlanListRoute(
    onShowSpendingPlanAdd: () -> Unit,
    onShowSpendingPlanEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: SpendingPlanListViewModel = hiltViewModel()
) {
    val spendingPlanListState by viewModel.spendingPlanListState.collectAsStateWithLifecycle()
    val spendingListViewMode by viewModel.spendingListViewMode.collectAsStateWithLifecycle()

    SpendingListScreen(
        spendingPlanListState = spendingPlanListState,
        spendingListViewMode = spendingListViewMode,
        onSpendingPlanClick = viewModel::changeSpendingSelected,
        onSpendingPlanAdd = onShowSpendingPlanAdd,
        onSpendingPlanEdit = viewModel::editSpending,
        onSpendingPlanDelete = viewModel::deleteSpending,
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
    spendingListViewMode: ViewMode,
    onSpendingPlanClick: (SpendingPlan) -> Unit,
    onSpendingPlanAdd: () -> Unit,
    onSpendingPlanEdit: () -> Unit,
    onSpendingPlanDelete: () -> Unit,
    onSpendingTabClick: (Int) -> Unit,
) {
    DefaultScaffold(
        color = Red3,
        bottomBarVisible = spendingListViewMode == ViewMode.EDIT,
        addButtonVisible = spendingListViewMode == ViewMode.LIST,
        onAdd = onSpendingPlanAdd,
        onEdit = onSpendingPlanEdit,
        onDelete = onSpendingPlanDelete,
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
                realTotalString = spendingPlanListState.realTotalString,
                totalString = spendingPlanListState.totalString,
                spendingPlanList = spendingPlanListState.filterSpendingPlanList,
                consumptionSpend = spendingPlanListState.filterConsumptionPlan,
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
            onSpendingPlanAdd = {},
            onSpendingPlanClick = {},
            onSpendingPlanEdit = {},
            onSpendingPlanDelete = {},
            onSpendingTabClick = {},
            spendingListViewMode = ViewMode.EDIT
        )
    }
}