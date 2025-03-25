package jun.money.mate.challenge

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.challenge.component.AmountOrCount
import jun.money.mate.challenge.component.ChallengeDateType
import jun.money.mate.challenge.contract.ChallengeAddEffect
import jun.money.mate.challenge.contract.ChallengeModalEffect
import jun.money.mate.challenge.contract.ChallengeAddState
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderLineText
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.save.ChallengeType
import jun.money.mate.ui.AddScaffold
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.ui.number.NumberKeyboard
import jun.money.mate.ui.number.ValueState

internal enum class ChallengeStep(
    val message: String
) {
    GoalAmount("챌린지 금액을 입력해 주세요"),
    AmountCount("1회 납입 금액이나 횟수를 선택해 주세요"),
    Remaining(""),
}

@Composable
internal fun ChallengeAddRoute(
    viewModel: ChallengeAddViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.background)

    val focusManager = LocalFocusManager.current
    val navigateAction = LocalNavigateActionInterop.current
    val uiState by viewModel.challengeAddState.collectAsStateWithLifecycle()
    val modalState by viewModel.challengeModalEffect.collectAsStateWithLifecycle()
    val showSnackBar = rememberShowSnackBar()
    val scrollState = rememberScrollState()

    AddScaffold(
        buttonText = when (viewModel.currentStep.value) {
            ChallengeStep.Remaining -> "추가"
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
            onChallengeTypeSelected = viewModel::challengeTypeSelected,
            onTitleChange = viewModel::titleChange,
            onAmountValueChange = viewModel::amountValueChange,
            onCountValueChange = viewModel::countValueChange,
            onComplete = viewModel::nextStep,
            isGoalAmountSelected = modalState == ChallengeModalEffect.ShowNumberKeyboard
        )
    }

    ChallengeModalContent(
        challengeModalEffect = modalState,
        onAmountChange = viewModel::goalAmountValueChange,
        onNumberDismissRequest = viewModel::numberKeyboardDismiss,
    )

    LaunchedEffect(Unit) {
        viewModel.challengeAddEffect.collect { effect ->
            when (effect) {
                ChallengeAddEffect.ChallengeAddComplete -> navigateAction.popBackStack()
                ChallengeAddEffect.RemoveTextFocus -> focusManager.clearFocus()
                is ChallengeAddEffect.ShowSnackBar -> showSnackBar(effect.messageType)
                ChallengeAddEffect.ScrollToBottom -> scrollState.animateScrollTo(scrollState.maxValue)
            }
        }
    }
}

@Composable
private fun ChallengeAddScreen(
    scrollState: ScrollState,
    currentStep: ChallengeStep,
    steps: List<ChallengeStep>,
    uiState: ChallengeAddState,
    onChallengeTypeSelected: (ChallengeType) -> Unit,
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
            visible = ChallengeStep.GoalAmount in steps,
            title = "챌린지 금액",
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
            visible = ChallengeStep.AmountCount in steps,
            title = "금액 / 횟수",
        ) {
            AmountOrCount(
                amountValue = uiState.amount,
                onAmountValueChange = onAmountValueChange,
                countValue = uiState.count,
                onCountValueChange = onCountValueChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
        TopToBottomAnimatedVisibility(ChallengeStep.Remaining in steps) {
            Column {
                ChallengeAddField(
                    title = "납입 주기",
                ) {
                    ChallengeDateType(
                        onChallengeTypeSelected = onChallengeTypeSelected,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                ChallengeAddField(
                    title = "설명",
                ) {
                    UnderlineTextField(
                        value = uiState.title,
                        onValueChange = onTitleChange,
                        hint = "챌린지의 설명을 입력해 주세요",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onComplete()
                            }
                        )
                    )
                }
            }
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
    challengeModalEffect: ChallengeModalEffect,
    onAmountChange: (ValueState) -> Unit,
    onNumberDismissRequest: () -> Unit,
) {
    when (challengeModalEffect) {
        ChallengeModalEffect.Idle -> {}
        ChallengeModalEffect.ShowNumberKeyboard -> {
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
private fun SaveAddScreenPreview() {
    JunTheme {
        ChallengeAddScreen(
            scrollState = rememberScrollState(),
            currentStep = ChallengeStep.GoalAmount,
            steps = ChallengeStep.entries,
            uiState = ChallengeAddState(),
            onShowNumberBottomSheet = {},
            onChallengeTypeSelected = {},
            onTitleChange = {},
            onComplete = {},
            onAmountValueChange = {},
            onCountValueChange = {},
        )
    }
}
