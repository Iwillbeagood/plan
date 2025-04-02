package jun.money.mate.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.budget.component.BudgetItem
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray9
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.consumption.Budget
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.utils.currency.CurrencyFormatter

/**
 * 일단 가장 위에 총액, 그냥 리스트로 보여주고 게이지로 보여주면 될듯..
 *
 * 예산에 달을 선택할 필요가 있을까..? 매달 그냥 리셋되면 될거같지만, 이전달에 어떻게 되었는지는 알려줘야 할거같은데,,,,
 *
 * 일단 예산은 매달 초에 리셋하고, 이전 기록으로 저장시켜야 할듯
 *
 * 예산은 그냥 보여주고 눌렀을 떄, 이전 기록도 보여주면 좋은데....
 *
 * */
@Composable
internal fun BudgetRoute(
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val showSnackBar = rememberShowSnackBar()
    val budgetUiState by viewModel.budgetState.collectAsStateWithLifecycle()
    val consumptionModalEffect by viewModel.consumptionModalEffect.collectAsStateWithLifecycle()
    val navigateAction = LocalNavigateActionInterop.current

    BudgetContent(
        budgetState = budgetUiState,
        viewModel = viewModel,
        onShowDetail = navigateAction::navigateToBudgetDetail
    )

    BudgetModalContent(
        modalEffect = consumptionModalEffect,
        viewModel = viewModel
    )

    LaunchedEffect(Unit) {
        viewModel.budgetEffect.collect { effect ->
            when (effect) {
                is BudgetEffect.ShowSnackBar -> showSnackBar(effect.messageType)
                BudgetEffect.NavigateToAdd -> {

                }
            }
        }
    }
}


@Composable
private fun BudgetContent(
    budgetState: BudgetState,
    viewModel: BudgetViewModel,
    onShowDetail: (Long) -> Unit,
) {
    FadeAnimatedVisibility(budgetState is BudgetState.BudgetData) {
        if (budgetState is BudgetState.BudgetData) {
            BudgetScreen(
                budgetState = budgetState,
                onShowBudgetAdd = viewModel::showBudgetAdd,
                onShowDetail = onShowDetail
            )
        }
    }
}

@Composable
private fun BudgetScreen(
    budgetState: BudgetState.BudgetData,
    onShowBudgetAdd: () -> Unit,
    onShowDetail: (Long) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column{
                VerticalSpacer(50.dp)
                Text(
                    text = "전체 예산",
                    style = TypoTheme.typography.titleMediumM,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 20.dp)
                )
                VerticalSpacer(4.dp)
                Text(
                    text = CurrencyFormatter.formatToWon(budgetState.totalBudget),
                    style = TypoTheme.typography.displayLargeB,
                    modifier = Modifier.padding(start = 20.dp)
                )
                VerticalSpacer(10.dp)
                RegularButton(
                    text = "예산 설정하기",
                    onClick = onShowBudgetAdd,
                    style = TypoTheme.typography.titleNormalB,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                HorizontalDivider(10.dp, Gray9)
                VerticalSpacer(30.dp)
                Text(
                    text = "예산 내역",
                    style = TypoTheme.typography.titleNormalM,
                    modifier = Modifier.padding(start = 20.dp)
                )
                VerticalSpacer(4.dp)
            }
        }
        items(budgetState.budgets) { budget ->
            BudgetItem(
                budget = budget,
                onClick = {
                    onShowDetail(budget.id)
                }
            )
        }
    }
}



@Composable
private fun BudgetModalContent(
    modalEffect: BudgetCostEffect,
    viewModel: BudgetViewModel,
) {
    when (modalEffect) {
        BudgetCostEffect.Hidden -> {

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingListScreenPreview() {
    JunTheme {
        BudgetScreen(
            budgetState = BudgetState.BudgetData(
                budgets = listOf(
                    Budget.sample,
                    Budget.sample,
                    Budget.sample,
                )
            ),
            onShowBudgetAdd = {},
            onShowDetail = {}
        )
    }
}