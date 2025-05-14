package jun.money.mate.budget

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.budget.contract.BudgetAddEffect
import jun.money.mate.budget.contract.BudgetAddModalEffect
import jun.money.mate.budget.contract.BudgetAddUiState
import jun.money.mate.budget.navigation.NAV_NAME
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderLineText
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.number.NumberKeyboard

internal enum class BudgetAddStep(
    val message: String,
) {
    Title("어떤 ${NAV_NAME}인지 설명해 주세요"),
    Amount("한달 ${NAV_NAME}을 설정해 주세요"),
}

@Composable
internal fun BudgetAddRoute(
    viewModel: BudgetAddViewModel = hiltViewModel(),
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

    val showSnackBar = rememberShowSnackBar()
    val navigateAction = LocalNavigateActionInterop.current
    val incomeAddState by viewModel.budgetAddUiState.collectAsStateWithLifecycle()
    val incomeModalEffect by viewModel.budgetAddModalEffect.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    AddScaffold(
        buttonText = when (viewModel.addStep.value) {
            BudgetAddStep.Title -> "다음"
            BudgetAddStep.Amount -> "추가"
        },
        onGoBack = navigateAction::popBackStack,
        onComplete = viewModel::nextStep,
    ) {
        BudgetAddScreen(
            addStep = viewModel.addStep.value,
            addSteps = viewModel.addSteps.value,
            uiState = incomeAddState,
            onNextStep = viewModel::nextStep,
            changeTitle = viewModel::titleValueChange,
            onShowNumberBottomSheet = viewModel::showNumberKeyboard,
        )
    }

    ModalContent(
        budgetAddModalEffect = incomeModalEffect,
        viewModel = viewModel,
    )

    LaunchedEffect(true) {
        viewModel.budgetAddEffect.collect {
            when (it) {
                is BudgetAddEffect.ShowSnackBar -> showSnackBar(it.messageType)
                BudgetAddEffect.BudgetAddComplete -> navigateAction.popBackStack()
                BudgetAddEffect.DismissKeyboard -> keyboardController?.hide()
                BudgetAddEffect.RemoveTitleFocus -> focusManager.clearFocus()
            }
        }
    }
}

@Composable
private fun BudgetAddScreen(
    addStep: BudgetAddStep,
    addSteps: List<BudgetAddStep>,
    uiState: BudgetAddUiState,
    onNextStep: () -> Unit,
    changeTitle: (String) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .animateContentSize(),
    ) {
        VerticalSpacer(36.dp)
        Text(
            text = addStep.message,
            style = TypoTheme.typography.titleLargeM,
        )
        VerticalSpacer(50.dp)
        BudgetField(
            visible = BudgetAddStep.Amount in addSteps,
            title = "금액",
        ) {
            Column {
                UnderLineText(
                    value = uiState.amountString,
                    hint = "선택",
                    modifier = Modifier.clickable(onClick = onShowNumberBottomSheet),
                )
                TopToBottomAnimatedVisibility(uiState.budget != 0L) {
                    Column {
                        VerticalSpacer(4.dp)
                        Text(
                            text = uiState.amountWon,
                            style = TypoTheme.typography.labelLargeM,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
        BudgetField(
            visible = BudgetAddStep.Title in addSteps,
            title = "제목",
        ) {
            UnderlineTextField(
                value = uiState.title,
                onValueChange = changeTitle,
                hint = "제목",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        onNextStep()
                    },
                ),
            )
        }
        VerticalSpacer(400.dp)
    }
}

@Composable
private fun BudgetField(
    visible: Boolean,
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
            VerticalSpacer(40.dp)
        }
    }
}

@Composable
private fun ModalContent(
    budgetAddModalEffect: BudgetAddModalEffect,
    viewModel: BudgetAddViewModel,
) {
    when (budgetAddModalEffect) {
        BudgetAddModalEffect.Idle -> {}
        BudgetAddModalEffect.ShowNumberKeyboard -> {
            NumberKeyboard(
                visible = true,
                onChangeNumber = viewModel::amountValueChange,
                onDismissRequest = {
                    viewModel.dismiss()
                    viewModel.nextStep()
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JunTheme {
        BudgetAddScreen(
            addStep = BudgetAddStep.Amount,
            addSteps = BudgetAddStep.entries,
            uiState = BudgetAddUiState(
                title = "월급",
                budget = 1000000,
            ),
            changeTitle = {},
            onShowNumberBottomSheet = {},
            onNextStep = {},
        )
    }
}
