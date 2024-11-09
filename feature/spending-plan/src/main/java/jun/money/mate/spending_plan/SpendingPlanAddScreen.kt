package jun.money.mate.spending_plan

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.DefaultTextField
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.TextButton
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem_date.datetimepicker.*
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingCategory.Companion.name
import jun.money.mate.model.spending.SpendingCategoryType
import jun.money.mate.model.spending.SpendingType
import jun.money.mate.model.spending.SpendingType.Companion.isConsumptionPlan
import jun.money.mate.model.spending.SpendingType.Companion.isPredictedSpending
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.spending_plan.component.CategoryIcon
import jun.money.mate.spending_plan.component.SpendingCategoryBottomSheet
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

@Composable
private fun SpendingPlanAddBody(
    title: String,
    amount: String,
    amountWon: String,
    date: String,
    type: SpendingType,
    spendingCategory: SpendingCategory,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
    onApplyType: (SpendingType) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        VerticalSpacer(20.dp)
        SpendingPlanTitle("지출 카테고리")
        Row {
            TextButton(
                text = SpendingType.ConsumptionPlan.title,
                text2 = "이번달에 소비할 지출 계획을 설정합니다.",
                onClick = { onApplyType(SpendingType.ConsumptionPlan) },
                color = if (type.isConsumptionPlan) Red3 else MaterialTheme.colorScheme.surfaceDim,
                textColor = if (type.isConsumptionPlan) White1 else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            HorizontalSpacer(10.dp)
            TextButton(
                text = SpendingType.PredictedSpending.title,
                text2 = "이번달에 예상되는 지출 금액을 설정합니다.",
                onClick = { onApplyType(SpendingType.PredictedSpending) },
                color = if (type.isPredictedSpending) Red3 else MaterialTheme.colorScheme.surfaceDim,
                textColor = if (type.isPredictedSpending) White1 else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
        }
        VerticalSpacer(20.dp)
        SpendingPlanTitle("지출명")
        DefaultTextField(
            value = title,
            onValueChange = onTitleChange,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            hint = "지출명을 입력해주세요"
        )

        VerticalSpacer(20.dp)
        SpendingPlanTitle("지출 금액")
        DefaultTextField(
            value = amount,
            onValueChange = onAmountChange,
            hint = "지출 금액을 입력해주세요",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.NumberPassword
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onShowCategoryBottomSheet()
                }
            )
        )
        TopToBottomAnimatedVisibility(amount.isNotEmpty()) {
            Text(
                text = amountWon,
                style = JUNTheme.typography.labelLargeM,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (type == SpendingType.PredictedSpending) {
            VerticalSpacer(20.dp)
            SpendingPlanTitle("지출 카테고리")
            TextButton(
                text = spendingCategory.name(),
                onClick = onShowCategoryBottomSheet,
                icon = {
                    if (spendingCategory is SpendingCategory.CategoryType) {
                        CategoryIcon(
                            category = spendingCategory.type
                        )
                    }
                }
            )
            VerticalSpacer(20.dp)
            SpendingPlanTitle("지출 예정 날짜")
            TextButton(
                text = date,
                onClick = onShowDateBottomSheet
            )
        }
    }
}

@Composable
private fun SpendingPlanTitle(title: String) {
    Text(
        text = title,
        style = JUNTheme.typography.titleMediumM,
    )
    VerticalSpacer(10.dp)
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
                type = SpendingType.ConsumptionPlan,
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
