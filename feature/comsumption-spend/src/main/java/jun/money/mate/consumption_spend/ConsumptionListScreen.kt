package jun.money.mate.consumption_spend

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.consumption_spend.component.ConsumptionListBody
import jun.money.mate.consumption_spend.component.SpendingCategoryBottomSheet
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.etc.EmptyMessage
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.model.consumption.Consumption
import jun.money.mate.model.consumption.ConsumptionFilter.Companion.selectedFilter
import jun.money.mate.model.etc.ViewMode
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.ui.DefaultScaffold
import java.time.LocalDate

@Composable
internal fun ConsumptionListRoute(
    onGoBack: () -> Unit,
    onShowConsumptionAdd: () -> Unit,
    onShowConsumptionEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: ConsumptionListViewModel = hiltViewModel()
) {
    val consumptionListState by viewModel.consumptionListState.collectAsStateWithLifecycle()
    val consumptionListViewMode by viewModel.consumptionListViewMode.collectAsStateWithLifecycle()
    val consumptionModalEffect by viewModel.consumptionModalEffect.collectAsStateWithLifecycle()
    val selectedDate by viewModel.dateState.collectAsStateWithLifecycle()
    val filterValue by viewModel.consumptionFilter.collectAsStateWithLifecycle()

    ConsumptionListScreen(
        consumptionListState = consumptionListState,
        filterValue = filterValue.selectedFilter().planTitle,
        consumptionListViewMode = consumptionListViewMode,
        selectedDate = selectedDate,
        onGoBack = onGoBack,
        onSpendingPlanAdd = onShowConsumptionAdd,
        onSpendingPlanEdit = viewModel::editSpending,
        onSpendingPlanDelete = viewModel::deleteSpending,
        onDateSelect = viewModel::dateSelected,
        onConsumptionAdd = onShowConsumptionAdd,
        onFilterClick = viewModel::showFilterBottomSheet,
        onConsumptionClick = viewModel::changeConsumptionSelected
    )

    ConsumptionModalContent(
        modalEffect = consumptionModalEffect,
        onCategorySelected = viewModel::filterClicked,
        onDismissRequest = viewModel::onDismissModal
    )

    LaunchedEffect(Unit) {
        viewModel.consumptionListEffect.collect { effect ->
            when (effect) {
                is ConsumptionListEffect.EditSpendingPlan -> onShowConsumptionEdit(effect.id)
                is ConsumptionListEffect.ShowSnackBar -> onShowSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun ConsumptionListScreen(
    consumptionListState: ConsumptionListState,
    filterValue: String,
    consumptionListViewMode: ViewMode,
    selectedDate: LocalDate,
    onGoBack: () -> Unit,
    onSpendingPlanAdd: () -> Unit,
    onSpendingPlanEdit: () -> Unit,
    onSpendingPlanDelete: () -> Unit,
    onDateSelect: (LocalDate) -> Unit,
    onConsumptionAdd: () -> Unit,
    onFilterClick: () -> Unit,
    onConsumptionClick: (Consumption) -> Unit,
) {
    DefaultScaffold(
        title = "지출 계획",
        color = Red3,
        bottomBarVisible = consumptionListViewMode == ViewMode.EDIT,
        addButtonVisible = consumptionListViewMode == ViewMode.LIST,
        selectedDate = selectedDate,
        onDateSelect = onDateSelect,
        onAdd = onSpendingPlanAdd,
        onEdit = onSpendingPlanEdit,
        onDelete = onSpendingPlanDelete,
        onGoBack = onGoBack,
    ) {
        SpendingListContent(
            consumptionListState = consumptionListState,
            filterValue = filterValue,
            onConsumptionAdd = onConsumptionAdd,
            onFilterClick = onFilterClick,
            onConsumptionClick = onConsumptionClick
        )
    }
}

@Composable
private fun SpendingListContent(
    consumptionListState: ConsumptionListState,
    filterValue: String,
    onConsumptionAdd: () -> Unit,
    onFilterClick: () -> Unit,
    onConsumptionClick: (Consumption) -> Unit,
) {
    FadeAnimatedVisibility(consumptionListState is ConsumptionListState.Empty) {
        if (consumptionListState is ConsumptionListState.Empty) {
            EmptyMessage("계획 소비가 없습니다.")
        }
    }

    FadeAnimatedVisibility(consumptionListState is ConsumptionListState.ConsumptionListData) {
        if (consumptionListState is ConsumptionListState.ConsumptionListData) {
            ConsumptionListBody(
                consumptionList = consumptionListState.consumptionList,
                filterValue = filterValue,
                onFilterClick = onFilterClick,
                onConsumptionAdd = onConsumptionAdd,
                onConsumptionClick = onConsumptionClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConsumptionModalContent(
    modalEffect: ConsumptionDialogEffect,
    onCategorySelected: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    when (modalEffect) {
        ConsumptionDialogEffect.Idle -> {}
        is ConsumptionDialogEffect.ShowFilterBottomSheet -> {
            SpendingCategoryBottomSheet(
                consumptionPlanTitles = modalEffect.filterTitles,
                onDismiss = onDismissRequest,
                onCategorySelected = onCategorySelected
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingListScreenPreview() {
    JunTheme {
        ConsumptionListScreen(
            consumptionListState = ConsumptionListState.Loading,
            filterValue = "전체",
            selectedDate = LocalDate.now(),
            onGoBack = {},
            onDateSelect = {},
            onSpendingPlanAdd = {},
            onSpendingPlanEdit = {},
            onSpendingPlanDelete = {},
            onConsumptionAdd = {},
            onFilterClick = {},
            onConsumptionClick = {},
            consumptionListViewMode = ViewMode.LIST
        )
    }
}