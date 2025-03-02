package jun.money.mate.spending_plan

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem_date.datetimepicker.DatePickerSheet
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingCategoryType
import jun.money.mate.model.spending.SpendingType
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.spending_plan.component.SpendingCategoryBottomSheet
import jun.money.mate.spending_plan.component.SpendingPlanAddBody
import jun.money.mate.ui.AddScaffold
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
    AddScaffold(
        color = Red3,
        onGoBack = onBackClick,
        onComplete = onAddIncome,
    ) {
        SpendingPlanAddContent(
            spendingPlanAddState = incomeAddState,
            onIncomeTitleChange = onIncomeTitleChange,
            onIncomeAmountChange = onIncomeAmountChange,
            onShowIncomeDateBottomSheet = onShowDateBottomSheet,
            onShowCategoryBottomSheet = onShowCategoryBottomSheet,
            onApplyType = onApplyType,
            onAddIncome = onAddIncome
        )
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
    onAddIncome: () -> Unit,
) {
    FadeAnimatedVisibility(spendingPlanAddState is SpendingPlanAddState.SpendingPlanData) {
        if (spendingPlanAddState is SpendingPlanAddState.SpendingPlanData) {
            SpendingPlanAddBody(
                title = spendingPlanAddState.title,
                amount = spendingPlanAddState.amountString,
                amountWon = spendingPlanAddState.amountWon,
                date = "${spendingPlanAddState.day}일",
                type = spendingPlanAddState.type,
                spendingCategory = spendingPlanAddState.spendingCategory,
                onTitleChange = onIncomeTitleChange,
                onAmountChange = onIncomeAmountChange,
                onShowDateBottomSheet = onShowIncomeDateBottomSheet,
                onShowCategoryBottomSheet = onShowCategoryBottomSheet,
                onApplyType = onApplyType,
                onAddIncome = onAddIncome
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
            DatePickerSheet(
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
                day = 12,
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
