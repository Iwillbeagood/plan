package jun.money.mate.consumption_spend

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
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
import jun.money.mate.consumption_spend.component.SpendingCategoryBottomSheet
import jun.money.mate.designsystem.component.DefaultTextField
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TextButton
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingType
import jun.money.mate.navigation.argument.AddType
import java.time.LocalDate

@Composable
internal fun ConsumptionAddRoute(
    addType: AddType,
    onGoBack: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: ConsumptionAddViewModel = hiltViewModel()
) {
    val consumptionAddState by viewModel.consumptionAddState.collectAsStateWithLifecycle()
    val consumptionModalEffect by viewModel.consumptionModalEffect.collectAsStateWithLifecycle()

    ConsumptionAddScreen(
        title = when (addType) {
            is AddType.Edit -> "수정"
            AddType.New -> "추가"
        },
        incomeAddState = consumptionAddState,
        onBackClick = onGoBack,
        onAddIncome = viewModel::addSpendingPlan,
        onIncomeTitleChange = viewModel::titleValueChange,
        onIncomeAmountChange = viewModel::amountValueChange,
        onShowDateBottomSheet = viewModel::showDatePicker,
        onShowCategoryBottomSheet = viewModel::showCategoryBottomSheet,
    )

    SpendingPlanModalContent(
        modalEffect = consumptionModalEffect,
        onDateSelect = viewModel::dateSelected,
        onCategorySelected = viewModel::categorySelected,
        onDismissRequest = viewModel::onDismiss
    )

    LaunchedEffect(true) {
        viewModel.consumptionAddEffect.collect {
            when (it) {
                is ConsumptionAddEffect.ShowSnackBar -> onShowSnackBar(it.messageType)
                ConsumptionAddEffect.SpendingPlanAddComplete -> onGoBack()
            }
        }
    }
}

@Composable
private fun ConsumptionAddScreen(
    title: String,
    incomeAddState: ConsumptionAddState,
    onBackClick: () -> Unit,
    onAddIncome: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onIncomeAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppbar(
                title = "계획 소비 $title",
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
            ConsumptionAddContent(
                consumptionAddState = incomeAddState,
                onIncomeTitleChange = onIncomeTitleChange,
                onIncomeAmountChange = onIncomeAmountChange,
                onShowIncomeDateBottomSheet = onShowDateBottomSheet,
                onShowCategoryBottomSheet = onShowCategoryBottomSheet,
            )
        }
    }
}

@Composable
private fun ConsumptionAddContent(
    consumptionAddState: ConsumptionAddState,
    onIncomeTitleChange: (String) -> Unit,
    onIncomeAmountChange: (String) -> Unit,
    onShowIncomeDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
) {
    FadeAnimatedVisibility(consumptionAddState is ConsumptionAddState.ConsumptionData) {
        if (consumptionAddState is ConsumptionAddState.ConsumptionData) {
            ConsumptionAddBody(
                title = consumptionAddState.title,
                amount = consumptionAddState.amountString,
                amountWon = consumptionAddState.amountWon,
                date = "${consumptionAddState.date.dayOfMonth}일",
                consumptionCategory = consumptionAddState.planTitle,
                onTitleChange = onIncomeTitleChange,
                onAmountChange = onIncomeAmountChange,
                onShowDateBottomSheet = onShowIncomeDateBottomSheet,
                onShowCategoryBottomSheet = onShowCategoryBottomSheet,
            )
        }
    }
}

@Composable
private fun ConsumptionAddBody(
    title: String,
    amount: String,
    amountWon: String,
    date: String,
    consumptionCategory: String,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        VerticalSpacer(20.dp)
        SpendingPlanTitle("소비명")
        DefaultTextField(
            value = title,
            onValueChange = onTitleChange,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            hint = "소비명을 입력해주세요"
        )

        VerticalSpacer(20.dp)
        SpendingPlanTitle("소비 금액")
        DefaultTextField(
            value = amount,
            onValueChange = onAmountChange,
            hint = "소비 금액을 입력해주세요",
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
        VerticalSpacer(20.dp)
        SpendingPlanTitle("계획한 지출")
        TextButton(
            text = consumptionCategory,
            onClick = onShowCategoryBottomSheet
        )
        VerticalSpacer(20.dp)
        SpendingPlanTitle("소비 날짜")
        TextButton(
            text = date,
            onClick = onShowDateBottomSheet
        )
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
    modalEffect: ConsumptionModalEffect,
    onDateSelect: (LocalDate) -> Unit,
    onCategorySelected: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    when (modalEffect) {
        ConsumptionModalEffect.Idle -> {}
        is ConsumptionModalEffect.ShowDatePicker -> {
            DatePicker(
                onDateSelect = onDateSelect,
                onDismissRequest = onDismissRequest,
            )
        }

        is ConsumptionModalEffect.ShowCategoryBottomSheet -> {
            SpendingCategoryBottomSheet(
                consumptionPlanTitles = modalEffect.consumptionPlanTitles,
                onDismiss = onDismissRequest,
                onCategorySelected = onCategorySelected
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConsumptionAddScreenPreview() {
    JunTheme {
        ConsumptionAddScreen(
            title = "추가",
            incomeAddState = ConsumptionAddState.ConsumptionData(
                id = 0,
                title = "월급",
                amount = 1000000,
                date = LocalDate.now(),
                planTitle = "",
            ),
            onIncomeTitleChange = {},
            onIncomeAmountChange = {},
            onShowDateBottomSheet = {},
            onShowCategoryBottomSheet = {},
            onBackClick = {},
            onAddIncome = {},
        )
    }
}
