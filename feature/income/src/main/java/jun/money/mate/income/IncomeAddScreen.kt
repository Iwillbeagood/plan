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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderLineText
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.income.contract.IncomeAddState
import jun.money.mate.income.contract.IncomeEffect
import jun.money.mate.income.contract.IncomeModalEffect
import jun.money.mate.model.etc.DateType
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.DateAdd
import jun.money.mate.ui.number.NumberKeyboard

internal enum class IncomeAddStep(
    val message: String,
) {
    Title("어떤 수입인지 설명해 주세요"),
    Amount("수입이 얼마나 발생됐는지 금액을 입력해 주세요"),
    Type("수입이 발생한 날짜를 선택해 주세요"),
}

@Composable
internal fun IncomeAddRoute(
    viewModel: IncomeAddViewModel = hiltViewModel(),
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

    val showSnackBar = rememberShowSnackBar()
    val navigateAction = LocalNavigateActionInterop.current
    val incomeAddState by viewModel.incomeAddState.collectAsStateWithLifecycle()
    val incomeModalEffect by viewModel.incomeModalEffect.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    AddScaffold(
        buttonText = when (viewModel.addStep.value) {
            IncomeAddStep.Title -> "다음"
            IncomeAddStep.Amount -> "다음"
            IncomeAddStep.Type -> "추가"
        },
        onGoBack = navigateAction::popBackStack,
        onComplete = viewModel::nextStep,
    ) {
        IncomeAddScreen(
            addStep = viewModel.addStep.value,
            addSteps = viewModel.addSteps.value,
            uiState = incomeAddState,
            onNextStep = viewModel::nextStep,
            onDismiss = viewModel::dismiss,
            onIncomeTitleChange = viewModel::titleValueChange,
            onShowNumberBottomSheet = viewModel::showNumberKeyboard,
            onDaySelected = viewModel::daySelected,
            onDateTypeSelected = viewModel::dateTypeSelected,
        )
    }

    IncomeModalContent(
        incomeModalEffect = incomeModalEffect,
        viewModel = viewModel,
    )

    LaunchedEffect(true) {
        viewModel.incomeEffect.collect {
            when (it) {
                is IncomeEffect.ShowSnackBar -> showSnackBar(it.messageType)
                IncomeEffect.IncomeComplete -> navigateAction.popBackStack()
                IncomeEffect.DismissKeyboard -> keyboardController?.hide()
                IncomeEffect.RemoveTitleFocus -> focusManager.clearFocus()
            }
        }
    }
}

@Composable
private fun IncomeAddScreen(
    addStep: IncomeAddStep,
    addSteps: List<IncomeAddStep>,
    uiState: IncomeAddState,
    onNextStep: () -> Unit,
    onDismiss: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
    onDaySelected: (Int) -> Unit,
    onDateTypeSelected: (DateType) -> Unit,
) {
    val requester by remember { mutableStateOf(FocusRequester()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .animateContentSize(),
    ) {
        VerticalSpacer(50.dp)
        Text(
            text = addStep.message,
            style = TypoTheme.typography.titleLargeM,
        )
        VerticalSpacer(50.dp)
        IncomeAddField(
            visible = IncomeAddStep.Type in addSteps,
            title = "날짜",
        ) {
            DateAdd(
                type = "수입",
                onDateSelected = onDaySelected,
                onDateTypeSelected = onDateTypeSelected,
                dateType = uiState.dateType,
            )
        }
        IncomeAddField(
            visible = IncomeAddStep.Amount in addSteps,
            title = "금액",
        ) {
            Column {
                UnderLineText(
                    value = uiState.amountString,
                    hint = "선택",
                    modifier = Modifier.clickable(onClick = onShowNumberBottomSheet),
                )
                TopToBottomAnimatedVisibility(uiState.amount != 0L) {
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
        IncomeAddField(
            visible = IncomeAddStep.Title in addSteps,
            title = "제목",
        ) {
            UnderlineTextField(
                value = uiState.title,
                onValueChange = onIncomeTitleChange,
                focusRequester = requester,
                hint = "수입 제목",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        onNextStep()
                    },
                ),
                onFocus = onDismiss,
            )
        }
        VerticalSpacer(400.dp)
    }

    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}

@Composable
private fun IncomeAddField(
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
private fun IncomeModalContent(
    incomeModalEffect: IncomeModalEffect,
    viewModel: IncomeAddViewModel,
) {
    when (incomeModalEffect) {
        IncomeModalEffect.Idle -> {}
        IncomeModalEffect.ShowNumberKeyboard -> {
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
private fun IncomeAddScreenPreview() {
    JunTheme {
        IncomeAddScreen(
            addStep = IncomeAddStep.Type,
            addSteps = listOf(IncomeAddStep.Title, IncomeAddStep.Amount, IncomeAddStep.Type),
            uiState = IncomeAddState(
                title = "월급",
                amount = 1000000,
                dateType = DateType.Monthly,
            ),
            onIncomeTitleChange = {},
            onShowNumberBottomSheet = {},
            onNextStep = {},
            onDaySelected = {},
            onDateTypeSelected = {},
            onDismiss = {},
        )
    }
}
