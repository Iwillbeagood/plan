package jun.money.mate.income

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.R
import jun.money.mate.designsystem.component.StateAnimatedVisibility
import jun.money.mate.designsystem.component.TopAppbarIcon
import jun.money.mate.designsystem.component.TwoBtnDialog
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.main10
import jun.money.mate.income.component.IncomeListBody
import jun.money.mate.income.component.LeavesBox
import jun.money.mate.income.contract.IncomeListEffect
import jun.money.mate.income.contract.IncomeListModalEffect
import jun.money.mate.income.contract.IncomeListState
import jun.money.mate.model.LeafOrder
import jun.money.mate.model.etc.EditMode
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.ui.EditModeButton
import jun.money.mate.utils.formatDateBasedOnYear
import java.time.YearMonth

@Composable
internal fun IncomeListRoute(
    viewModel: IncomeListViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(main10)
    val navigateAction = LocalNavigateActionInterop.current
    val showSnackBar = rememberShowSnackBar()

    val incomeListState by viewModel.incomeListState.collectAsStateWithLifecycle()
    val modalEffect by viewModel.modalEffect.collectAsStateWithLifecycle()
    val leaves by viewModel.leaves.collectAsStateWithLifecycle()
    val month by viewModel.month.collectAsStateWithLifecycle()

    IncomeListScreen(
        leaves = leaves,
        incomeListState = incomeListState,
        month = month,
        onPrev = viewModel::prevMonth,
        onNext = viewModel::nextMonth,
        onGoBack = navigateAction::popBackStack,
        onShowIncomeAdd = navigateAction::navigateToIncomeAdd,
        onIncomeClick = viewModel::selectIncome,
        onDeleteSelectedIncome = viewModel::showDeleteDialog,
        onEditSelectedIncome = viewModel::editIncome,
    )

    IncomeListModalContent(
        modalEffect = modalEffect,
        viewModel = viewModel
    )

    LaunchedEffect(Unit) {
        viewModel.incomeListEffect.collect { effect ->
            when (effect) {
                is IncomeListEffect.EditIncome -> navigateAction.navigateToIncomeEdit(effect.id)
                is IncomeListEffect.ShowSnackBar -> showSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun IncomeListScreen(
    leaves: List<LeafOrder>,
    incomeListState: IncomeListState,
    month: YearMonth,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onGoBack: () -> Unit,
    onShowIncomeAdd: () -> Unit,
    onIncomeClick: (Income) -> Unit,
    onDeleteSelectedIncome: () -> Unit,
    onEditSelectedIncome: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            EditModeButton(
                editMode = (incomeListState as? IncomeListState.UiData)?.editMode ?: EditMode.LIST,
                onAdd = onShowIncomeAdd,
                onDelete = onDeleteSelectedIncome,
                onEdit = onEditSelectedIncome,
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(main10)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LeavesBox(
                    leaves = leaves,
                    modifier = Modifier.weight(4f)
                )
                IncomeListContent(
                    month = month,
                    onPrev = onPrev,
                    onNext = onNext,
                    incomeListState = incomeListState,
                    onIncomeClick = onIncomeClick,
                    modifier = Modifier.weight(6f)
                )
            }
            StateAnimatedVisibility<IncomeListState.UiData>(
                target = incomeListState,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Column(
                    modifier = Modifier.padding(start = 30.dp, top = 60.dp)
                ) {
                    Text(
                        text = "${formatDateBasedOnYear(month)} 수입",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = TypoTheme.typography.headlineSmallM,
                    )
                    Text(
                        text = it.incomeList.totalString,
                        style = TypoTheme.typography.displaySmallB,
                    )
                }
            }
            TopAppbarIcon(
                icon = Icons.Default.ArrowBackIosNew,
                onClick = onGoBack,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun IncomeListContent(
    month: YearMonth,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    incomeListState: IncomeListState,
    onIncomeClick: (Income) -> Unit,
    modifier: Modifier = Modifier
) {
    StateAnimatedVisibility<IncomeListState.UiData>(
        target = incomeListState,
        modifier = modifier
    ) {
        IncomeListBody(
            month = month,
            onPrev = onPrev,
            onNext = onNext,
            incomeList = it.incomeList,
            onIncomeClick = onIncomeClick
        )
    }
}

@Composable
private fun IncomeListModalContent(
    modalEffect: IncomeListModalEffect,
    viewModel: IncomeListViewModel
) {
    when (modalEffect) {
        IncomeListModalEffect.Hidden -> {}
        IncomeListModalEffect.ShowDeleteConfirmDialog -> {
            TwoBtnDialog(
                onDismissRequest = viewModel::hideModal,
                button2Text = stringResource(id = jun.money.mate.res.R.string.btn_yes),
                button2Click = viewModel::deleteIncome,
                content = {
                    Text(
                        text = "선택한 수입을 삭제하시겠습니까?",
                        style = TypoTheme.typography.titleMediumM
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IncomeListScreenPreview() {
    JunTheme {
        IncomeListScreen(
            leaves = List(5) { LeafOrder(isRed = it % 2 == 0) },
            incomeListState = IncomeListState.UiData(
                incomeList = IncomeList.sample
            ),
            month = YearMonth.now(),
            onPrev = {},
            onNext = {},
            onGoBack = {},
            onShowIncomeAdd = {},
            onIncomeClick = {},
            onDeleteSelectedIncome = {},
            onEditSelectedIncome = {},
        )
    }
}