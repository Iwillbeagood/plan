package jun.money.mate.income

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.StateAnimatedVisibility
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderLineText
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.income.contract.EditState
import jun.money.mate.income.contract.IncomeEffect
import jun.money.mate.income.contract.IncomeModalEffect
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.income.Income
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.DateAdd
import jun.money.mate.ui.number.NumberKeyboard
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun IncomeEditRoute(
    viewModel: IncomeEditViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.background)

    val showSnackBar = rememberShowSnackBar()
    val navigateAction = LocalNavigateActionInterop.current
    val editState by viewModel.editState.collectAsStateWithLifecycle()
    val incomeModalEffect by viewModel.incomeModalEffect.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    AddScaffold(
        buttonText = "수정하기",
        onGoBack = navigateAction::popBackStack,
        onComplete = viewModel::editIncome,
    ) {
        IncomeAddContent(
            editState = editState,
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
                is IncomeEffect.ShowSnackBar -> showSnackBar(it.messageType)
                IncomeEffect.IncomeComplete -> navigateAction.popBackStack()
                IncomeEffect.DismissKeyboard -> keyboardController?.hide()
                IncomeEffect.RemoveTitleFocus -> focusManager.clearFocus()
            }
        }
    }
}

@Composable
private fun IncomeAddContent(
    editState: EditState,
    viewModel: IncomeEditViewModel
) {
    StateAnimatedVisibility<EditState.UiData>(
        target = editState,
    ) {
        IncomeEditBlock(
            uiState = it,
            onIncomeTitleChange = viewModel::titleValueChange,
            onShowNumberBottomSheet = viewModel::showNumberKeyboard,
            onDaySelected = viewModel::daySelected,
            onDateSelected = viewModel::dateSelected,
        )
    }
}

@Composable
private fun IncomeEditBlock(
    uiState: EditState.UiData,
    onIncomeTitleChange: (String) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
    onDaySelected: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    val listState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(listState)
            .animateContentSize()
    ) {
        VerticalSpacer(50.dp)
        EditContent(
            title = "수입 제목",
        ) {
            UnderlineTextField(
                value = uiState.title,
                onValueChange = onIncomeTitleChange,
                hint = "수입 제목"
            )
        }
        EditContent(
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
                        style = TypoTheme.typography.labelLargeM,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        }
        EditContent(
            title = "수입 날짜",
        ) {
            DateAdd(
                type = "수입",
                onDaySelected = onDaySelected,
                onDateSelected = onDateSelected,
                originDateType = uiState.dateType
            )
        }
    }
}

@Composable
private fun EditContent(
    title: String,
    content: @Composable () -> Unit,
) {
    Column {
        Text(
            text = title,
            style = TypoTheme.typography.labelLargeM,
        )
        content()
        VerticalSpacer(30.dp)
    }
}

@Composable
private fun IncomeModalContent(
    incomeModalEffect: IncomeModalEffect,
    viewModel: IncomeEditViewModel
) {
    when (incomeModalEffect) {
        IncomeModalEffect.Idle -> {}
        IncomeModalEffect.ShowNumberKeyboard -> {
            NumberKeyboard(
                visible = true,
                onChangeNumber = viewModel::amountValueChange,
                onDismissRequest = viewModel::dismiss,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IncomeAddScreenPreview() {
    JunTheme {
        IncomeEditBlock(
            uiState = EditState.UiData(
                id = 1L,
                title = "수입 제목",
                amount = 1000L,
                dateType = DateType.Monthly(1, YearMonth.now()),
                originIncome = Income.regularSample
            ),
            onIncomeTitleChange = {},
            onShowNumberBottomSheet = {},
            onDaySelected = {},
            onDateSelected = {},
        )
    }
}
