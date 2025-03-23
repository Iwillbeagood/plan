package jun.money.mate.cost

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.cost.contract.CostAddEffect
import jun.money.mate.cost.contract.CostModalEffect
import jun.money.mate.cost.component.CostTypeSelector
import jun.money.mate.cost.contract.CostAddState
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderLineText
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.spending.CostType
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.interop.LocalNavigateActionInterop
import jun.money.mate.ui.interop.rememberShowSnackBar
import jun.money.mate.ui.number.NumberKeyboard
import jun.money.mate.ui.number.ValueState

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
    ChangeStatusBarColor(MaterialTheme.colorScheme.background)

    val focusManager = LocalFocusManager.current
    val navigateAction = LocalNavigateActionInterop.current
    val uiState by viewModel.costAddState.collectAsStateWithLifecycle()
    val modalState by viewModel.costModalEffect.collectAsStateWithLifecycle()
    val showSnackBar = rememberShowSnackBar()
    val scrollState = rememberScrollState()

    AddScaffold(
        buttonText = when (viewModel.currentStep.value) {
            CostStep.Date -> "추가"
            else -> "다음"
        },
        onGoBack = navigateAction::popBackStack,
        onComplete = viewModel::nextStep
    ) {
        ChallengeAddScreen(
            scrollState = scrollState,
            currentStep = viewModel.currentStep.value,
            steps = viewModel.addSteps.value,
            uiState = uiState,
            onShowNumberBottomSheet = viewModel::showNumberKeyboard,
            onCostTypeSelected = viewModel::costTypeSelected,
            onTitleChange = viewModel::titleChange,
            onAmountValueChange = viewModel::amountValueChange,
            onCountValueChange = viewModel::countValueChange,
            onComplete = viewModel::nextStep,
            isGoalAmountSelected = modalState == CostModalEffect.ShowNumberKeyboard
        )
    }

    ChallengeModalContent(
        costModalEffect = modalState,
        onAmountChange = viewModel::goalAmountValueChange,
        onNumberDismissRequest = viewModel::numberKeyboardDismiss,
    )

    LaunchedEffect(Unit) {
        viewModel.costAddEffect.collect { effect ->
            when (effect) {
                CostAddEffect.CostAddComplete -> navigateAction.popBackStack()
                CostAddEffect.RemoveTextFocus -> focusManager.clearFocus()
                is CostAddEffect.ShowSnackBar -> showSnackBar(effect.messageType)
                CostAddEffect.ScrollToBottom -> scrollState.animateScrollTo(scrollState.maxValue)
            }
        }
    }
}

@Composable
private fun ChallengeAddScreen(
    scrollState: ScrollState,
    currentStep: CostStep,
    steps: List<CostStep>,
    uiState: CostAddState,
    onCostTypeSelected: (CostType?) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
    onTitleChange: (String) -> Unit,
    onAmountValueChange: (String) -> Unit,
    onCountValueChange: (String) -> Unit,
    onComplete: () -> Unit,
    isGoalAmountSelected: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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
            title = "납입 주기",
        ) {
        }
        ChallengeAddField(
            visible = CostStep.Amount in steps,
            title = "소비 금액",
        ) {
            Column {
                UnderLineText(
                    value = uiState.goalAmountString,
                    hint = "선택",
                    isSelected = isGoalAmountSelected,
                    modifier = Modifier.clickable(onClick = onShowNumberBottomSheet),
                )
                TopToBottomAnimatedVisibility(uiState.goalAmount != 0L) {
                    Column {
                        VerticalSpacer(4.dp)
                        Text(
                            text = uiState.goalAmountWon,
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

@Composable
private fun ChallengeModalContent(
    costModalEffect: CostModalEffect,
    onAmountChange: (ValueState) -> Unit,
    onNumberDismissRequest: () -> Unit,
) {
    when (costModalEffect) {
        CostModalEffect.Idle -> {}
        CostModalEffect.ShowNumberKeyboard -> {
            NumberKeyboard(
                visible = true,
                buttonText = "다음",
                onChangeNumber = onAmountChange,
                onDismissRequest = onNumberDismissRequest,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CostAddScreenPreview() {
    JunTheme {
        ChallengeAddScreen(
            scrollState = rememberScrollState(),
            currentStep = CostStep.CostType,
            steps = CostStep.entries,
            uiState = CostAddState(),
            onShowNumberBottomSheet = {},
            onCostTypeSelected = {},
            onTitleChange = {},
            onComplete = {},
            onAmountValueChange = {},
            onCountValueChange = {},
        )
    }
}
