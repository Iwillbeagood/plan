package jun.money.mate.income

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderLineText
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.main
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.designsystem_date.datetimepicker.DayPicker
import jun.money.mate.designsystem_date.datetimepicker.TimeBoundaries
import jun.money.mate.income.component.TypeButton
import jun.money.mate.income.contract.IncomeEffect
import jun.money.mate.income.contract.IncomeModalEffect
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.number.NumberKeyboard
import java.time.LocalDate

internal enum class IncomeAddStep(
    val message: String
) {
    Title("어떤 수입인지 설명해 주세요"),
    Amount("수입이 얼마나 발생됐는지 금액을 입력해 주세요"),
    Type("수입이 발생한 날짜를 선택해 주세요");
}

@Composable
internal fun IncomeAddRoute(
    onGoBack: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: IncomeAddViewModel = hiltViewModel()
) {
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
        color = main,
        onGoBack = onGoBack,
        onComplete = viewModel::nextStep
    ) {
        IncomeAddContent(
            addStep = viewModel.addStep.value,
            addSteps = viewModel.addSteps.value,
            incomeAddState = incomeAddState,
            viewModel = viewModel
        )
    }

    IncomeModalContent(
        incomeModalEffect = incomeModalEffect,
        viewModel = viewModel
    )

    LaunchedEffect(true) {
        viewModel.incomeEffect.collect {
            when (it) {
                is IncomeEffect.ShowSnackBar -> onShowSnackBar(it.messageType)
                IncomeEffect.IncomeComplete -> onGoBack()
                IncomeEffect.DismissKeyboard -> keyboardController?.hide()
                IncomeEffect.RemoveTitleFocus -> focusManager.clearFocus()
            }
        }
    }
}

@Composable
private fun IncomeAddContent(
    addStep: IncomeAddStep,
    addSteps: List<IncomeAddStep>,
    incomeAddState: IncomeAddState,
    viewModel: IncomeAddViewModel
) {
    IncomeAddScreen(
        addStep = addStep,
        addSteps = addSteps,
        uiState = incomeAddState,
        onNextStep = viewModel::nextStep,
        onIncomeTitleChange = viewModel::titleValueChange,
        onShowNumberBottomSheet = viewModel::showNumberKeyboard,
        onDaySelected = viewModel::daySelected,
        onDateSelected = viewModel::dateSelected,
    )
}

@Composable
private fun IncomeAddScreen(
    addStep: IncomeAddStep,
    addSteps: List<IncomeAddStep>,
    uiState: IncomeAddState,
    onNextStep: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
    onDaySelected: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .animateContentSize()
    ) {
        VerticalSpacer(50.dp)
        Text(
            text = addStep.message,
            style = JUNTheme.typography.titleLargeM,
        )
        VerticalSpacer(50.dp)
        IncomeAddBlock(
            addSteps = addSteps,
            uiState = uiState,
            onNextStep = onNextStep,
            onIncomeTitleChange = onIncomeTitleChange,
            onShowNumberBottomSheet = onShowNumberBottomSheet,
            onDaySelected = onDaySelected,
            onDateSelected = onDateSelected,
        )
        VerticalSpacer(400.dp)
    }
}

@Composable
private fun IncomeAddBlock(
    addSteps: List<IncomeAddStep>,
    uiState: IncomeAddState,
    onNextStep: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
    onDaySelected: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    IncomeAddBlock(
        visible = IncomeAddStep.Type in addSteps,
        title = "날짜",
    ) {
        DateAdd(
            onDaySelected = onDaySelected,
            onDateSelected = onDateSelected,
        )
    }
    IncomeAddBlock(
        visible = IncomeAddStep.Amount in addSteps,
        title = "수입 금액",
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
                        style = JUNTheme.typography.labelLargeM,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }
    IncomeAddBlock(
        visible = IncomeAddStep.Title in addSteps,
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
}

@Composable
private fun DateAdd(
    onDaySelected: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    var isMonthly by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TypeButton(
                text = "정기 수익",
                isType = isMonthly == true,
                onApplyType = {
                    isMonthly = true
                },
                modifier = Modifier.weight(1f)
            )
            HorizontalSpacer(10.dp)
            TypeButton(
                text = "단기 수익",
                isType = isMonthly == false,
                onApplyType = {
                    isMonthly = false
                },
                modifier = Modifier.weight(1f)
            )
        }
        VerticalSpacer(16.dp)
        Crossfade(
            isMonthly
        ) {
            when (it) {
                true -> {
                    DayPicker(
                        onDaySelected = onDaySelected,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                false -> {
                    DatePicker(
                        timeBoundary = TimeBoundaries.lastMonthToThisMonth,
                        onDateSelect = onDateSelected,
                    )
                }

                null -> {}
            }
        }
    }
}


@Composable
private fun IncomeAddBlock(
    visible: Boolean,
    title: String,
    content: @Composable () -> Unit,
) {
    TopToBottomAnimatedVisibility(visible) {
        Column {
            Text(
                text = title,
                style = JUNTheme.typography.labelLargeM,
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
    viewModel: IncomeAddViewModel
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
                dateType = DateType.Monthly(1),
            ),
            onIncomeTitleChange = {},
            onShowNumberBottomSheet = {},
            onNextStep = {},
            onDaySelected = {},
            onDateSelected = {},
        )
    }
}
