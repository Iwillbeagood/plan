package jun.money.mate.spending_plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingCategoryType
import jun.money.mate.model.spending.SpendingType
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.spending_plan.component.SpendingCategoryBottomSheet
import jun.money.mate.spending_plan.component.SpendingPlanAddBody
import java.time.LocalDate

@Composable
internal fun SpendingPlanAddRoute(
    addType: AddType,
    onGoBack: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: SpendingPlanAddViewModel = hiltViewModel()
) {
    val spendingPlanAddState by viewModel.spendingPlanAddState.collectAsStateWithLifecycle()
    val spendingPlanModalEffect by viewModel.spendingPlanModalEffect.collectAsStateWithLifecycle()

    SpendingPlanAddScreen(
        title = when (addType) {
            is AddType.Edit -> "수정"
            AddType.New -> "추가"
        },
        incomeAddState = spendingPlanAddState,
        onBackClick = onGoBack,
        onAddIncome = viewModel::addSpendingPlan,
        onIncomeTitleChange = viewModel::titleValueChange,
        onIncomeAmountChange = viewModel::amountValueChange,
        onShowDateBottomSheet = viewModel::showDatePicker,
        onShowCategoryBottomSheet = viewModel::showCategoryBottomSheet,
        onApplyType = viewModel::applyTypeSelected
    )

    SpendingPlanModalContent(
        incomeModalEffect = spendingPlanModalEffect,
        onDateSelect = viewModel::dateSelected,
        onCategorySelected = viewModel::categorySelected,
        onDismissRequest = viewModel::onDismiss
    )

    LaunchedEffect(true) {
        viewModel.spendingPlanAddEffect.collect {
            when (it) {
                is SpendingPlanAddEffect.ShowSnackBar -> onShowSnackBar(it.messageType)
                SpendingPlanAddEffect.SpendingPlanAddComplete -> onGoBack()
            }
        }
    }
}

@Composable
private fun SpendingPlanAddScreen(
    title: String,
    incomeAddState: SpendingPlanAddState,
    onBackClick: () -> Unit,
    onAddIncome: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onIncomeAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
    onApplyType: (SpendingType) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppbar(
                title = "지출 계획 $title",
                onBackEvent = onBackClick
            )
        },
        bottomBar = {
            RegularButton(
                text = "${title}하기",
                color = Red3,
                onClick = onAddIncome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .padding(horizontal = 10.dp),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(it)
        ) {
            SpendingPlanAddContent(
                spendingPlanAddState = incomeAddState,
                onIncomeTitleChange = onIncomeTitleChange,
                onIncomeAmountChange = onIncomeAmountChange,
                onShowIncomeDateBottomSheet = onShowDateBottomSheet,
                onShowCategoryBottomSheet = onShowCategoryBottomSheet,
                onApplyType = onApplyType
            )
        }
    }
}

@Composable
private fun SpendingPlanAddContent(
    spendingPlanAddState: SpendingPlanAddState,
    onIncomeTitleChange: (String) -> Unit,
    onIncomeAmountChange: (String) -> Unit,
    onShowIncomeDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
    onApplyType: (SpendingType) -> Unit,
) {
    FadeAnimatedVisibility(spendingPlanAddState is SpendingPlanAddState.SpendingPlanData) {
        if (spendingPlanAddState is SpendingPlanAddState.SpendingPlanData) {
            SpendingPlanAddBody(
                title = spendingPlanAddState.title,
                amount = spendingPlanAddState.amountString,
                amountWon = spendingPlanAddState.amountWon,
                date = "${spendingPlanAddState.date.dayOfMonth}일",
                type = spendingPlanAddState.type,
                spendingCategory = spendingPlanAddState.spendingCategory,
                onTitleChange = onIncomeTitleChange,
                onAmountChange = onIncomeAmountChange,
                onShowDateBottomSheet = onShowIncomeDateBottomSheet,
                onShowCategoryBottomSheet = onShowCategoryBottomSheet,
                onApplyType = onApplyType
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpendingPlanModalContent(
    incomeModalEffect: SpendingPlanModalEffect,
    onDateSelect: (LocalDate) -> Unit,
    onCategorySelected: (SpendingCategoryType) -> Unit,
    onDismissRequest: () -> Unit,
) {
    when (incomeModalEffect) {
        SpendingPlanModalEffect.Idle -> {}
        is SpendingPlanModalEffect.ShowDatePicker -> {
            DatePicker(
                onDateSelect = onDateSelect,
                onDismissRequest = onDismissRequest,
            )
        }

        is SpendingPlanModalEffect.ShowCategoryBottomSheet -> {
            SpendingCategoryBottomSheet(
                onDismiss = onDismissRequest,
                onCategorySelected = onCategorySelected
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingPlanAddScreenPreview() {
    JunTheme {
        SpendingPlanAddScreen(
            title = "추가",
            incomeAddState = SpendingPlanAddState.SpendingPlanData(
                id = 0,
                title = "월급",
                amount = 1000000,
                date = LocalDate.now(),
                type = SpendingType.PredictedSpending,
                spendingCategory = SpendingCategory.NotSelected
            ),
            onIncomeTitleChange = {},
            onIncomeAmountChange = {},
            onShowDateBottomSheet = {},
            onShowCategoryBottomSheet = {},
            onBackClick = {},
            onAddIncome = {},
            onApplyType = {}
        )
    }
}
