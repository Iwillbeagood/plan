package jun.money.mate.cost

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.cost.component.CostTypeSelector
import jun.money.mate.cost.contract.CostAddEffect
import jun.money.mate.cost.contract.CostAddState
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.spending.CostType
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.DateAdd
import java.time.LocalDate

internal enum class CostStep(
    val message: String
) {
    CostType("소비의 종류를 입력해 주세요"),
    Amount("소비 금액을 입력해 주세요"),
    Date("소비가 발생할 날짜를 입력해 주세요"),
}

@Composable
internal fun CostAddRoute(
    viewModel: CostAddViewModel = hiltViewModel()
) {
    ChangeStatusBarColor()

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
        ChallengeAddScreen(
            currentStep = viewModel.currentStep.value,
            steps = viewModel.addSteps.value,
            uiState = uiState,
            onCostTypeSelected = viewModel::costTypeSelected,
            onAmountValueChange = viewModel::amountValueChange,
            onDaySelected = viewModel::daySelected,
            onDateSelected = viewModel::dateSelected,
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
private fun ChallengeAddScreen(
    currentStep: CostStep,
    steps: List<CostStep>,
    uiState: CostAddState,
    onCostTypeSelected: (CostType?) -> Unit,
    onAmountValueChange: (String) -> Unit,
    onDaySelected: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onNextStep: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .animateContentSize()
    ) {
        VerticalSpacer(50.dp)
        if (currentStep.message.isNotBlank()) {
            Text(
                text = currentStep.message,
                style = TypoTheme.typography.titleLargeM,
            )
            VerticalSpacer(40.dp)
        }
        ChallengeAddField(
            visible = CostStep.Date in steps,
            title = "소비 날짜",
        ) {
            DateAdd(
                type = "소비",
                onDaySelected = onDaySelected,
                onDateSelected = onDateSelected,
            )
        }
        ChallengeAddField(
            visible = CostStep.Amount in steps,
            title = "소비 금액",
        ) {
            Column {
                UnderlineTextField(
                    value = uiState.amountString,
                    onValueChange = onAmountValueChange,
                    hint = "금액을 입력해 주세요",
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
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
        ChallengeAddField(
            visible = CostStep.CostType in steps,
            title = "소비 유형",
        ) {
            CostTypeSelector(
                onSelected = onCostTypeSelected,
                costType = uiState.costType
            )
        }
        VerticalSpacer(100.dp)
    }
}

@Composable
private fun ChallengeAddField(
    visible: Boolean = true,
    title: String,
    content: @Composable () -> Unit,
) {
    TopToBottomAnimatedVisibility(visible) {
        Column {
            Text(
                text = title,
                style = TypoTheme.typography.labelLargeM,
            )
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
        ChallengeAddScreen(
            currentStep = CostStep.CostType,
            steps = CostStep.entries,
            uiState = CostAddState(),
            onCostTypeSelected = {},
            onDaySelected = {},
            onDateSelected = {},
            onAmountValueChange = {},
            onNextStep = {},
        )
    }
}
