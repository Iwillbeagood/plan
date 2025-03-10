package jun.money.mate.consumption_spend

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.consumption_spend.component.ConsumptionListBody
import jun.money.mate.consumption_spend.component.SpendingCategoryBottomSheet
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.TwoBtnDialog
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.model.consumption.Consumption
import jun.money.mate.model.consumption.ConsumptionFilter.Companion.selectedFilter
import jun.money.mate.model.etc.ViewMode
import jun.money.mate.ui.DateScaffold
import jun.money.mate.ui.interop.rememberPopBackStack
import jun.money.mate.ui.interop.rememberShowSnackBar
import java.time.LocalDate

@Composable
internal fun ConsumptionListRoute(
    onShowConsumptionAdd: () -> Unit,
    onShowSpendingPlanAdd: () -> Unit,
    onShowConsumptionEdit: (id: Long) -> Unit,
    viewModel: ConsumptionListViewModel = hiltViewModel()
) {
    val showSnackBar = rememberShowSnackBar()
    val popBackStack = rememberPopBackStack()
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
        onSpendingPlanEdit = viewModel::editSpending,
        onSpendingPlanDelete = viewModel::deleteSpending,
        onDateSelect = viewModel::dateSelected,
        onConsumptionAdd = viewModel::showConsumptionAdd,
        onFilterClick = viewModel::showFilterBottomSheet,
        onConsumptionClick = viewModel::changeConsumptionSelected
    )

    ConsumptionModalContent(
        modalEffect = consumptionModalEffect,
        onCategorySelected = viewModel::filterClicked,
        onDismissRequest = viewModel::onDismissModal,
        onShowSpendingPlanAdd = onShowSpendingPlanAdd
    )

    LaunchedEffect(Unit) {
        viewModel.consumptionListEffect.collect { effect ->
            when (effect) {
                is ConsumptionListEffect.EditSpendingPlan -> onShowConsumptionEdit(effect.id)
                is ConsumptionListEffect.ShowSnackBar -> showSnackBar(effect.messageType)
                ConsumptionListEffect.ShowConsumptionAdd -> onShowConsumptionAdd()
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
    onSpendingPlanEdit: () -> Unit,
    onSpendingPlanDelete: () -> Unit,
    onDateSelect: (LocalDate) -> Unit,
    onConsumptionAdd: () -> Unit,
    onFilterClick: () -> Unit,
    onConsumptionClick: (Consumption) -> Unit,
) {
    DateScaffold(
        color = Red3,
        bottomBarVisible = consumptionListViewMode == ViewMode.EDIT,
        addButtonVisible = false,
        selectedDate = selectedDate,
        onDateSelect = onDateSelect,
        onAdd = onConsumptionAdd,
        onEdit = onSpendingPlanEdit,
        onDelete = onSpendingPlanDelete,
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
    onShowSpendingPlanAdd: () -> Unit
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

        ConsumptionDialogEffect.ShowSpendingPlanDialog -> {
            TwoBtnDialog(
                title = "지출계획 추가",
                onDismissRequest = onDismissRequest,
                button2Text = stringResource(id = jun.money.mate.res.R.string.btn_yes),
                button2Click = {
                    onShowSpendingPlanAdd()
                    onDismissRequest()
                },
                content = {
                    Text(
                        text = "소비를 추가하기 위해서는 먼저 이번달에 소비할 지출 계획을 추가해야 합니다.",
                        style = TypoTheme.typography.titleMediumR
                    )
                    VerticalSpacer(10.dp)
                    Text(
                        text = "지출 계획 추가 페이지로 이동하시겠습니까?",
                        style = TypoTheme.typography.titleMediumM,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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
            onDateSelect = {},
            onSpendingPlanEdit = {},
            onSpendingPlanDelete = {},
            onConsumptionAdd = {},
            onFilterClick = {},
            onConsumptionClick = {},
            consumptionListViewMode = ViewMode.LIST
        )
    }
}