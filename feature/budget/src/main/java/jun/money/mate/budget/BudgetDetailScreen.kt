package jun.money.mate.budget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.budget.component.EditBudgetSheet
import jun.money.mate.budget.component.PastBudgetChart
import jun.money.mate.budget.component.UsedAddSheet
import jun.money.mate.budget.component.UsedAddSheetMode
import jun.money.mate.budget.component.UsedStateFeedback
import jun.money.mate.budget.component.usedList
import jun.money.mate.budget.contract.BudgetDetailEffect
import jun.money.mate.budget.contract.BudgetDetailModalState
import jun.money.mate.budget.contract.BudgetDetailState
import jun.money.mate.budget.navigation.NAV_NAME
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.StateAnimatedVisibility
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.component.TopAppbarIcon
import jun.money.mate.designsystem.component.TopAppbarType
import jun.money.mate.designsystem.component.TwoBtnDialog
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray4
import jun.money.mate.designsystem.theme.Gray9
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.consumption.Used
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.res.R
import jun.money.mate.ui.EditSheet
import jun.money.mate.utils.currency.CurrencyFormatter

@Composable
internal fun BudgetDetailRoute(
    viewModel: BudgetDetailViewModel = hiltViewModel(),
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

    val navigateAction = LocalNavigateActionInterop.current
    val showSnackBar = rememberShowSnackBar()
    val uiState by viewModel.budgetDetailState.collectAsStateWithLifecycle()
    val modalState by viewModel.modalState.collectAsStateWithLifecycle()

    BudgetDetailScreen(
        uiState = uiState,
        isEditingBudget = modalState is BudgetDetailModalState.ShowEditBudgetSheet,
        onBackEvent = navigateAction::popBackStack,
        onEditBudget = viewModel::showEditBudgetSheet,
        onEditBudgetByFeedback = viewModel::showEditBudgetSheetByFeedback,
        onDeleteBudget = viewModel::showDeleteDialog,
        onAddUsed = viewModel::showAddUsedSheet,
        onClickUsed = viewModel::selectUsed,
        onEditUsed = viewModel::editUsed,
        onDeleteUsed = viewModel::deleteUsed,
        ooUnSelectUsed = viewModel::unSelectUsed,
    )

    ModalContent(
        modalState = modalState,
        viewModel = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.budgetDetailEffect.collect {
            when (it) {
                BudgetDetailEffect.PopBackStack -> navigateAction.popBackStack()
                is BudgetDetailEffect.ShowSnackBar -> showSnackBar(it.messageType)
            }
        }
    }
}

@Composable
private fun BudgetDetailScreen(
    uiState: BudgetDetailState,
    isEditingBudget: Boolean,
    onBackEvent: () -> Unit,
    onClickUsed: (Used) -> Unit,
    onEditBudget: () -> Unit,
    onDeleteBudget: () -> Unit,
    onAddUsed: () -> Unit,
    onEditBudgetByFeedback: () -> Unit,
    onEditUsed: () -> Unit,
    onDeleteUsed: () -> Unit,
    ooUnSelectUsed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppbar(
                title = (uiState as? BudgetDetailState.BudgetDetailData)?.budget?.title ?: "",
                onBackEvent = onBackEvent,
                navigationType = TopAppbarType.Custom {
                    TopAppbarIcon(
                        icon = Icons.Default.Delete,
                        onClick = onDeleteBudget,
                    )
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            StateAnimatedVisibility<BudgetDetailState.BudgetDetailData>(
                target = uiState,
            ) { state ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    BudgetDetailContent(
                        budget = state.budget,
                        isEditingBudget = isEditingBudget,
                        onClickUsed = onClickUsed,
                        onEditBudget = onEditBudget,
                        onAddUsed = onAddUsed,
                        onEditBudgetByFeedback = onEditBudgetByFeedback,
                    )
                    EditSheet(
                        selectedCount = state.selectedCount,
                        onEdit = onEditUsed,
                        onClose = ooUnSelectUsed,
                        onDelete = onDeleteUsed,
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 16.dp)
                            .align(Alignment.BottomCenter),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BudgetDetailContent(
    budget: Budget,
    isEditingBudget: Boolean,
    onClickUsed: (Used) -> Unit,
    onAddUsed: () -> Unit,
    onEditBudget: () -> Unit,
    onEditBudgetByFeedback: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        stickyHeader {
            BudgetDetailHeader(
                budget = budget,
                onEditBudget = onEditBudget,
                isEditingBudget = isEditingBudget,
            )
        }

        item {
            if (budget.pastBudgets.isNotEmpty()) {
                Column {
                    PastBudgetChart(
                        originBudget = budget.budget,
                        pastBudgetGroup = budget.groupedPastBudget,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                    UsedStateFeedback(
                        usedState = budget.usedState,
                        maxUse = budget.maxUse,
                        onClick = onEditBudgetByFeedback,
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }

        item {
            Column {
                HorizontalDivider(4.dp, Gray9)
                VerticalSpacer(16.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    Text(
                        text = "내역",
                        style = TypoTheme.typography.titleMediumM,
                    )
                    HorizontalSpacer(1f)
                    TopAppbarIcon(
                        icon = Icons.Default.Add,
                        onClick = onAddUsed,
                    )
                }
            }
        }
        usedList(
            usedList = budget.usedList,
            onClickUsed = onClickUsed,
        )
    }
}

@Composable
private fun BudgetDetailHeader(
    budget: Budget,
    isEditingBudget: Boolean,
    onEditBudget: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
    ) {
        VerticalSpacer(20.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "잔액",
                style = TypoTheme.typography.titleNormalM,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier,
            )
            HorizontalSpacer(1f)
            Text(
                text = CurrencyFormatter.formatAmountWon(budget.budgetLeft),
                style = TypoTheme.typography.displayLargeB,
                color = if (budget.budgetLeft > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            )
        }
        VerticalSpacer(4.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = NAV_NAME,
                style = TypoTheme.typography.titleNormalM,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier,
            )
            HorizontalSpacer(1f)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(onClick = onEditBudget)
                    .background(if (isEditingBudget) Gray4.copy(alpha = 0.2f) else Color.Transparent),
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                HorizontalSpacer(4.dp)
                Text(
                    text = CurrencyFormatter.formatAmountWon(budget.budget),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = TypoTheme.typography.headlineSmallB,
                )
            }
        }
        VerticalSpacer(20.dp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalContent(
    modalState: BudgetDetailModalState,
    viewModel: BudgetDetailViewModel,
) {
    when (modalState) {
        BudgetDetailModalState.Hidden -> {}
        is BudgetDetailModalState.ShowEditBudgetSheet -> {
            EditBudgetSheet(
                recommend = modalState.recommend,
                onDismissRequest = viewModel::hideModal,
                onEditBudget = viewModel::editBudget,
            )
        }
        BudgetDetailModalState.ShowDeleteDialog -> {
            TwoBtnDialog(
                onDismissRequest = viewModel::hideModal,
                button2Text = stringResource(id = R.string.btn_yes),
                button2Click = viewModel::deleteBudget,
                content = {
                    Text(
                        text = "${NAV_NAME}를 삭제하시겠습니까?",
                        style = TypoTheme.typography.titleMediumM,
                    )
                },
            )
        }
        BudgetDetailModalState.ShowAddUsedSheet -> {
            UsedAddSheet(
                mode = UsedAddSheetMode.ADD,
                onDismissRequest = viewModel::hideModal,
                onComplete = viewModel::addUsed,
            )
        }
        is BudgetDetailModalState.ShowEditUsedSheet -> {
            UsedAddSheet(
                mode = UsedAddSheetMode.EDIT,
                originUsed = modalState.originUsed,
                onDismissRequest = viewModel::hideModal,
                onComplete = viewModel::editUsed,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    JunTheme {
        BudgetDetailScreen(
            uiState = BudgetDetailState.BudgetDetailData(
                budget = Budget.sample,
            ),
            isEditingBudget = true,
            onBackEvent = {},
            onClickUsed = {},
            onEditBudget = {},
            onDeleteBudget = {},
            onAddUsed = {},
            onEditBudgetByFeedback = {},
            onEditUsed = {},
            onDeleteUsed = {},
            ooUnSelectUsed = {},
        )
    }
}
