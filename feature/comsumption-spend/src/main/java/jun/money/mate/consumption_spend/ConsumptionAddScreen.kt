package jun.money.mate.consumption_spend

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import jun.money.mate.designsystem.component.TextButton
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem_date.datetimepicker.DatePickerSheet
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.AddTitleContent
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import java.time.LocalDate

@Composable
internal fun ConsumptionAddRoute(
    addType: AddType,
    viewModel: ConsumptionAddViewModel = hiltViewModel()
) {
    val showSnackBar = rememberShowSnackBar()
    val navigateAction = LocalNavigateActionInterop.current
    val consumptionAddState by viewModel.consumptionAddState.collectAsStateWithLifecycle()
    val consumptionModalEffect by viewModel.consumptionModalEffect.collectAsStateWithLifecycle()

    ConsumptionAddScreen(
        title = when (addType) {
            is AddType.Edit -> "수정"
            AddType.New -> "추가"
        },
        consumptionAddState = consumptionAddState,
        onBackClick = navigateAction::popBackStack,
        onConsumptionAdd = viewModel::addSpendingPlan,
        onTitleChange = viewModel::titleValueChange,
        onAmountChange = viewModel::amountValueChange,
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
                is ConsumptionAddEffect.ShowSnackBar -> showSnackBar(it.messageType)
                ConsumptionAddEffect.SpendingPlanAddComplete -> navigateAction.popBackStack()
            }
        }
    }
}

@Composable
private fun ConsumptionAddScreen(
    title: String,
    consumptionAddState: ConsumptionAddState,
    onBackClick: () -> Unit,
    onConsumptionAdd: () -> Unit,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
) {
    AddScaffold(
        title = "계획 소비 $title",
        color = Red3,
        onGoBack = onBackClick,
        onComplete = onConsumptionAdd,
    ) {
        ConsumptionAddContent(
            consumptionAddState = consumptionAddState,
            onConsumptionAdd = onConsumptionAdd,
            onTitleChange = onTitleChange,
            onAmountChange = onAmountChange,
            onShowDateBottomSheet = onShowDateBottomSheet,
            onShowCategoryBottomSheet = onShowCategoryBottomSheet,
        )
    }
}

@Composable
private fun ConsumptionAddContent(
    consumptionAddState: ConsumptionAddState,
    onConsumptionAdd: () -> Unit,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
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
                onConsumptionAdd = onConsumptionAdd,
                onTitleChange = onTitleChange,
                onAmountChange = onAmountChange,
                onShowDateBottomSheet = onShowDateBottomSheet,
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
    onConsumptionAdd: () -> Unit,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
) {
    val listState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(listState)
            .animateContentSize()
    ) {
        AddTitleContent("계획한 지출") {
            TextButton(
                text = consumptionCategory,
                onClick = onShowCategoryBottomSheet
            )
        }
        AddTitleContent("소비 날짜") {
            TextButton(
                text = date,
                onClick = onShowDateBottomSheet
            )
        }
        AddTitleContent(
            title = "소비명",
            visible = consumptionCategory.isNotEmpty()
        ) {
            DefaultTextField(
                value = title,
                onValueChange = onTitleChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                hint = "소비명을 입력해주세요"
            )
        }
        AddTitleContent(
            title = "소비 금액",
            visible = consumptionCategory.isNotEmpty()
        ) {
            DefaultTextField(
                value = amount,
                onValueChange = onAmountChange,
                hint = "소비 금액을 입력해주세요",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.NumberPassword
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onConsumptionAdd()
                    }
                )
            )
            TopToBottomAnimatedVisibility(amount.isNotEmpty()) {
                Text(
                    text = amountWon,
                    style = TypoTheme.typography.labelLargeM,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        VerticalSpacer(30.dp)
    }
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
            DatePickerSheet(
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
            consumptionAddState = ConsumptionAddState.ConsumptionData(
                id = 0,
                title = "월급",
                amount = 1000000,
                date = LocalDate.now(),
                planTitle = "",
            ),
            onTitleChange = {},
            onAmountChange = {},
            onShowDateBottomSheet = {},
            onShowCategoryBottomSheet = {},
            onBackClick = {},
            onConsumptionAdd = {},
        )
    }
}
