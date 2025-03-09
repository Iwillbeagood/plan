package jun.money.mate.income

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
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
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import jun.money.mate.ui.EditModeButton
import jun.money.mate.utils.formatDateBasedOnYear
import java.time.LocalDate
import java.time.YearMonth

/**
 *  정기 수입을 수정했을 떄가 문제가 될듯.
 *  이전 수입은 변경되면 안되고, 이후
 * */
@Composable
internal fun IncomeListRoute(
    onGoBack: () -> Unit,
    onShowIncomeAdd: () -> Unit,
    onShowIncomeEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: IncomeListViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(main10)

    val incomeListState by viewModel.incomeListState.collectAsStateWithLifecycle()
    val modalEffect by viewModel.modalEffect.collectAsStateWithLifecycle()
    val leaves by viewModel.leaves.collectAsStateWithLifecycle()

    IncomeListScreen(
        leaves = leaves,
        incomeListState = incomeListState,
        month = viewModel.month,
        onGoBack = onGoBack,
        onShowIncomeAdd = onShowIncomeAdd,
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
                is IncomeListEffect.EditIncome -> onShowIncomeEdit(effect.id)
                is IncomeListEffect.ShowSnackBar -> onShowSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun IncomeListScreen(
    leaves: List<LeafOrder>,
    incomeListState: IncomeListState,
    month: YearMonth,
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
        containerColor = main10
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                    incomeListState = incomeListState,
                    onIncomeClick = onIncomeClick,
                    modifier = Modifier.weight(6f)
                )
            }
            FadeAnimatedVisibility(
                visible = incomeListState is IncomeListState.UiData,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                if (incomeListState is IncomeListState.UiData) {
                    Column(
                        modifier = Modifier.padding(start = 30.dp, top = 60.dp)
                    ) {
                        Text(
                            text = "${formatDateBasedOnYear(month)} 수입",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = TypoTheme.typography.headlineSmallM,
                        )
                        Text(
                            text = incomeListState.incomeList.totalString,
                            style = TypoTheme.typography.displaySmallB,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable(onClick = onGoBack)
                    .align(Alignment.TopStart),
            ) {
                TopAppbarIcon(
                    iconId = R.drawable.ic_back,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun IncomeListContent(
    incomeListState: IncomeListState,
    onIncomeClick: (Income) -> Unit,
    modifier: Modifier = Modifier
) {
    FadeAnimatedVisibility(
        visible = incomeListState is IncomeListState.UiData,
        modifier = modifier
    ) {
        if (incomeListState is IncomeListState.UiData) {
            IncomeListBody(
                incomeList = incomeListState.incomeList,
                onIncomeClick = onIncomeClick
            )
        }
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
            onGoBack = {},
            onShowIncomeAdd = {},
            onIncomeClick = {},
            onDeleteSelectedIncome = {},
            onEditSelectedIncome = {},
        )
    }
}