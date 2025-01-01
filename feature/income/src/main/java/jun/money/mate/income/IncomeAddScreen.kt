package jun.money.mate.income

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.BottomToTopAnimatedVisibility
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderLineText
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.main
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.income.IncomeAddState.Companion.buttonText
import jun.money.mate.income.component.IncomeTypeBottomSheet
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.IncomeType
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.number.NumberKeyboard
import jun.money.mate.ui.number.ValueState
import java.time.LocalDate

internal enum class IncomeAddStep(
    val message: String
) {
    Type("먼저 수입 유형을 선택해 주세요"),
    Title("어떤 수입인지 설명해 주세요"),
    Amount("수입이 얼마나 발생됐는지 금액을 입력해 주세요"),
    Date("수입이 발생한 날짜를 선택해 주세요");

    companion object {
        val startStep = Type
        val endStep = Date

        fun IncomeAddStep.nextStep(): IncomeAddStep = entries.toTypedArray().let {
            val nextIndex = it.indexOf(this) + 1
            if (nextIndex < it.size) it[nextIndex] else it.last()
        }
    }
}

@Composable
internal fun IncomeAddRoute(
    addType: AddType,
    onGoBack: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: IncomeAddViewModel = hiltViewModel()
) {
    val incomeAddState by viewModel.incomeAddState.collectAsStateWithLifecycle()
    val incomeModalEffect by viewModel.incomeModalEffect.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    IncomeAddScreen(
        title = when (addType) {
            is AddType.Edit -> "수정"
            AddType.New -> "추가"
        },
        incomeAddState = incomeAddState,
        onBackClick = onGoBack,
        onNextStep = viewModel::nextStep,
        onIncomeTitleChange = viewModel::onTitleValueChange,
        onShowNumberBottomSheet = viewModel::showNumberKeyboard,
        onShowIncomeDateBottomSheet = viewModel::showDatePicker,
        onShowIncomeTypeBottomSheet = viewModel::showTypePicker,
    )

    IncomeModalContent(
        incomeModalEffect = incomeModalEffect,
        onDateSelect = viewModel::onDateSelected,
        onDismissRequest = viewModel::dismiss,
        onTypeSelected = viewModel::incomeTypeSelected,
        onAmountChange = viewModel::amountValueChange,
        onNumberDismissRequest = viewModel::numberKeyboardDismiss,
    )

    LaunchedEffect(true) {
        viewModel.incomeAddEffect.collect {
            when (it) {
                is IncomeAddEffect.ShowSnackBar -> onShowSnackBar(it.messageType)
                IncomeAddEffect.IncomeAddComplete -> onGoBack()
                IncomeAddEffect.DismissKeyboard -> keyboardController?.hide()
                IncomeAddEffect.RemoveTitleFocus -> focusManager.clearFocus()
            }
        }
    }
}

@Composable
private fun IncomeAddScreen(
    title: String,
    incomeAddState: IncomeAddState,
    onBackClick: () -> Unit,
    onNextStep: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
    onShowIncomeTypeBottomSheet: () -> Unit,
    onShowIncomeDateBottomSheet: () -> Unit,
) {
    AddScaffold(
        title = "수입 $title",
        buttonText = incomeAddState.buttonText(),
        color = main,
        onGoBack = onBackClick,
        onComplete = onNextStep
    ) {
        IncomeAddContent(
            incomeAddState = incomeAddState,
            onNextStep = onNextStep,
            onIncomeTitleChange = onIncomeTitleChange,
            onShowNumberBottomSheet = onShowNumberBottomSheet,
            onShowIncomeDateBottomSheet = onShowIncomeDateBottomSheet,
            onShowIncomeTypeBottomSheet = onShowIncomeTypeBottomSheet,
        )
    }
}

@Composable
private fun IncomeAddContent(
    incomeAddState: IncomeAddState,
    onNextStep: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
    onShowIncomeDateBottomSheet: () -> Unit,
    onShowIncomeTypeBottomSheet: () -> Unit,
) {
    FadeAnimatedVisibility(incomeAddState is IncomeAddState.UiData) {
        if (incomeAddState is IncomeAddState.UiData) {
            IncomeAddBody(
                uiState = incomeAddState,
                onNextStep = onNextStep,
                onIncomeTitleChange = onIncomeTitleChange,
                onShowNumberBottomSheet = onShowNumberBottomSheet,
                onShowIncomeDateBottomSheet = onShowIncomeDateBottomSheet,
                onShowIncomeTypeBottomSheet = onShowIncomeTypeBottomSheet,
            )
        }
    }
}

@Composable
private fun IncomeAddBody(
    uiState: IncomeAddState.UiData,
    onNextStep: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
    onShowIncomeDateBottomSheet: () -> Unit,
    onShowIncomeTypeBottomSheet: () -> Unit,
) {
    val listState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(listState)
            .animateContentSize()
    ) {
        VerticalSpacer(50.dp)
        Text(
            text = uiState.currentStep.message,
            style = JUNTheme.typography.titleLargeM,
        )
        VerticalSpacer(20.dp)
        IncomeAddStepContent(
            uiState = uiState,
            onNextStep = onNextStep,
            onIncomeTitleChange = onIncomeTitleChange,
            onShowNumberBottomSheet = onShowNumberBottomSheet,
            onShowIncomeDateBottomSheet = onShowIncomeDateBottomSheet,
            onShowIncomeTypeBottomSheet = onShowIncomeTypeBottomSheet,
        )
    }
}

@Composable
private fun IncomeAddStepContent(
    uiState: IncomeAddState.UiData,
    onNextStep: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
    onShowIncomeDateBottomSheet: () -> Unit,
    onShowIncomeTypeBottomSheet: () -> Unit,
) {
    val steps = uiState.incomeAddSteps
    val currentStep = uiState.currentStep

    IncomeAddStepContent(
        step = IncomeAddStep.Date,
        steps = steps,
        currentStep = currentStep,
        title = "수입 날짜",
    ) {
        UnderLineText(
            value = uiState.date.toString(),
            modifier = Modifier.clickable(onClick = onShowIncomeDateBottomSheet),
        )
    }
    IncomeAddStepContent(
        step = IncomeAddStep.Amount,
        steps = steps,
        currentStep = currentStep,
        title = "수입 금액",
    ) {
        Column {
            UnderLineText(
                value = uiState.amountString,
                hint = "선택",
                modifier = Modifier.clickable(onClick = onShowNumberBottomSheet),
            )
            TopToBottomAnimatedVisibility(uiState.amount != 0L) {
                Text(
                    text = uiState.amountWon,
                    style = JUNTheme.typography.labelLargeM,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
    IncomeAddStepContent(
        step = IncomeAddStep.Title,
        steps = steps,
        currentStep = currentStep,
        title = "수입 제목",
    ) {
        UnderlineTextField(
            value = uiState.title,
            onValueChange = onIncomeTitleChange,
            hint = "수입 제목",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNextStep()
                }
            )
        )
    }
    IncomeAddStepContent(
        step = IncomeAddStep.Type,
        steps = steps,
        currentStep = currentStep,
        title = "수입 유형",
    ) {
        UnderLineText(
            value = uiState.type?.title ?: "",
            hint = "선택",
            modifier = Modifier.clickable(onClick = onShowIncomeTypeBottomSheet),
        )
    }
}

@Composable
private fun IncomeAddStepContent(
    step: IncomeAddStep,
    steps: List<IncomeAddStep>,
    currentStep: IncomeAddStep,
    title: String,
    content: @Composable () -> Unit,
) {
    TopToBottomAnimatedVisibility(step in steps) {
        Column {
            BottomToTopAnimatedVisibility(currentStep != step) {
                Text(
                    text = title,
                    style = JUNTheme.typography.labelLargeM,
                )
            }
            content()
            VerticalSpacer(30.dp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IncomeModalContent(
    incomeModalEffect: IncomeModalEffect,
    onNumberDismissRequest: () -> Unit,
    onAmountChange: (ValueState) -> Unit,
    onTypeSelected: (IncomeType) -> Unit,
    onDateSelect: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
) {
    when (incomeModalEffect) {
        IncomeModalEffect.Idle -> {}
        IncomeModalEffect.ShowTypePicker -> {
            IncomeTypeBottomSheet(
                onDismiss = onDismissRequest,
                onTypeSelected = onTypeSelected,
            )
        }
        is IncomeModalEffect.ShowDatePicker -> {
            DatePicker(
                onDateSelect = onDateSelect,
                onDismissRequest = onDismissRequest,
            )
        }
        IncomeModalEffect.ShowNumberKeyboard -> {
            NumberKeyboard(
                visible = true,
                onChangeNumber = onAmountChange,
                onDismissRequest = onNumberDismissRequest,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IncomeAddScreenPreview() {
    JunTheme {
        IncomeAddScreen(
            title = "추가",
            incomeAddState = IncomeAddState.UiData(
                id = 0,
                title = "월급",
                amount = 1000000,
                date = LocalDate.now(),
                type = IncomeType.REGULAR,
                incomeAddSteps = IncomeAddStep.entries,
            ),
            onIncomeTitleChange = {},
            onShowIncomeDateBottomSheet = {},
            onShowIncomeTypeBottomSheet = {},
            onShowNumberBottomSheet = {},
            onBackClick = {},
            onNextStep = {},
        )
    }
}
