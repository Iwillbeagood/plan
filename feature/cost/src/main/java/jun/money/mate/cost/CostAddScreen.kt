package jun.money.mate.cost

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.cost.component.CostTypeSelector
import jun.money.mate.cost.contract.CostAddEffect
import jun.money.mate.cost.contract.CostAddState
import jun.money.mate.cost.navigation.Title
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem_date.datetimepicker.DayPicker
import jun.money.mate.model.spending.CostType
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.ui.AddScaffold


internal enum class CostStep(
    val message: String
) {
    CostType("${Title}의 종류를 입력해 주세요"),
    Amount("지출 금액을 입력해 주세요"),
    Date("${Title}이 발생할 날짜를 입력해 주세요"),
}

@Composable
internal fun CostAddRoute(
    viewModel: CostAddViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

    val focusManager = LocalFocusManager.current
    val navigateAction = LocalNavigateActionInterop.current
    val uiState by viewModel.costAddState.collectAsStateWithLifecycle()
    val showSnackBar = rememberShowSnackBar()

    AddScaffold(
        buttonText = when (viewModel.currentStep.value) {
            CostStep.CostType -> ""
            CostStep.Amount -> "다음"
            CostStep.Date -> "추가"
        },
        buttonVisible = viewModel.currentStep.value != CostStep.CostType,
        onGoBack = navigateAction::popBackStack,
        onComplete = viewModel::nextStep
    ) {
        CostAddScreen(
            currentStep = viewModel.currentStep.value,
            steps = viewModel.addSteps.value,
            uiState = uiState,
            onCostTypeSelected = viewModel::costTypeSelected,
            onAmountValueChange = viewModel::amountValueChange,
            onDaySelected = viewModel::daySelected,
            onNextStep = viewModel::nextStep,
        )
    }

    LaunchedEffect(Unit) {
        viewModel.costAddEffect.collect { effect ->
            when (effect) {
                CostAddEffect.CostAddComplete -> navigateAction.popBackStack()
                CostAddEffect.RemoveTextFocus -> focusManager.clearFocus()
                is CostAddEffect.ShowSnackBar -> showSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun CostAddScreen(
    currentStep: CostStep,
    steps: List<CostStep>,
    uiState: CostAddState,
    onCostTypeSelected: (CostType?) -> Unit,
    onAmountValueChange: (String) -> Unit,
    onDaySelected: (String) -> Unit,
    onNextStep: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .animateContentSize()
    ) {
        VerticalSpacer(30.dp)
        if (currentStep.message.isNotBlank()) {
            Text(
                text = currentStep.message,
                style = TypoTheme.typography.titleLargeM,
            )
            VerticalSpacer(20.dp)
        }
        CostAddField(
            title = "$Title 날짜",
            isCurrentStep = CostStep.Date == currentStep,
            visible = CostStep.Date in steps,
        ) {
            DayPicker(
                onDaySelected = onDaySelected,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        CostAddField(
            title = "지출 금액",
            isCurrentStep = currentStep == CostStep.Amount,
            visible = CostStep.Amount in steps,
        ) {
            Column {
                UnderlineTextField(
                    value = uiState.amountString,
                    onValueChange = onAmountValueChange,
                    hint = "금액을 입력해 주세요",
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            onNextStep()
                        }
                    )
                )
                TopToBottomAnimatedVisibility(uiState.amount != 0L) {
                    Column {
                        VerticalSpacer(4.dp)
                        Text(
                            text = uiState.amountWon,
                            style = TypoTheme.typography.labelLargeM,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        CostAddField(
            title = "지출 유형",
            isCurrentStep = currentStep == CostStep.CostType,
            visible = CostStep.CostType in steps,
        ) {
            CostTypeSelector(
                onSelected = onCostTypeSelected,
                costType = uiState.costType
            )
        }
    }
}

@Composable
private fun CostAddField(
    title: String,
    isCurrentStep: Boolean,
    visible: Boolean = true,
    content: @Composable () -> Unit,
) {
    TopToBottomAnimatedVisibility(visible) {
        Column {
            if (!isCurrentStep) {
                Text(
                    text = title,
                    style = TypoTheme.typography.labelLargeM,
                )
            }
            VerticalSpacer(10.dp)
            content()
            VerticalSpacer(30.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CostAddScreenPreview() {
    JunTheme {
        CostAddScreen(
            currentStep = CostStep.CostType,
            steps = CostStep.entries,
            uiState = CostAddState(),
            onCostTypeSelected = {},
            onDaySelected = {},
            onAmountValueChange = {},
            onNextStep = {},
        )
    }
}
