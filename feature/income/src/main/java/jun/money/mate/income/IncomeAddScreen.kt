package jun.money.mate.income

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.HmDefaultTextField
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.LargeButton
import jun.money.mate.designsystem.component.NonTextField
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.IncomeType
import jun.money.mate.navigation.argument.AddType
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun IncomeAddRoute(
    addType: AddType,
    onGoBack: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: IncomeAddViewModel = hiltViewModel()
) {
    val incomeAddState by viewModel.incomeAddState.collectAsStateWithLifecycle()
    val incomeModalEffect by viewModel.incomeModalEffect.collectAsStateWithLifecycle()

    IncomeAddScreen(
        title = when (addType) {
            is AddType.Edit -> "수정"
            AddType.New -> "추가"
        },
        incomeAddState = incomeAddState,
        onBackClick = onGoBack,
        onAddIncome = viewModel::onAddIncome,
        onIncomeTitleChange = viewModel::onTitleValueChange,
        onIncomeAmountChange = viewModel::onAmountValueChange,
        onShowIncomeDateBottomSheet = viewModel::onShowDatePicker,
        onRegularIncomeClick = viewModel::onRegularIncomeClick,
        onVariableIncomeClick = viewModel::onVariableIncomeClick,
    )

    IncomeModalContent(
        incomeModalEffect = incomeModalEffect,
        onDateSelect = viewModel::onDateSelected,
        onDismissRequest = viewModel::onDismiss
    )

    LaunchedEffect(true) {
        viewModel.incomeAddEffect.collect {
            when (it) {
                is IncomeAddEffect.ShowSnackBar -> onShowSnackBar(it.messageType)
                IncomeAddEffect.IncomeAddComplete -> onGoBack()
            }
        }
    }
}

@Composable
private fun IncomeAddScreen(
    title: String,
    incomeAddState: IncomeAddState,
    onBackClick: () -> Unit,
    onAddIncome: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onIncomeAmountChange: (String) -> Unit,
    onShowIncomeDateBottomSheet: () -> Unit,
    onRegularIncomeClick: () -> Unit,
    onVariableIncomeClick: () -> Unit,

    ) {
    Scaffold(
        topBar = {
            TopAppbar(
                title = "수입 $title",
                onBackEvent = onBackClick
            )
        },
        bottomBar = {
            LargeButton(
                text = "${title}하기",
                onClick = onAddIncome,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(it)
        ) {
            IncomeAddContent(
                incomeAddState = incomeAddState,
                onAddIncome = onAddIncome,
                onIncomeTitleChange = onIncomeTitleChange,
                onIncomeAmountChange = onIncomeAmountChange,
                onShowIncomeDateBottomSheet = onShowIncomeDateBottomSheet,
                onRegularIncomeClick = onRegularIncomeClick,
                onVariableIncomeClick = onVariableIncomeClick,
            )
        }
    }
}

@Composable
private fun IncomeAddContent(
    incomeAddState: IncomeAddState,
    onAddIncome: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onIncomeAmountChange: (String) -> Unit,
    onShowIncomeDateBottomSheet: () -> Unit,
    onRegularIncomeClick: () -> Unit,
    onVariableIncomeClick: () -> Unit,
) {
    FadeAnimatedVisibility(incomeAddState is IncomeAddState.IncomeData) {
        if (incomeAddState is IncomeAddState.IncomeData) {
            IncomeAddBody(
                regularIncomeSelected = incomeAddState.regularIncomeSelected,
                variableIncomeSelected = incomeAddState.variableIncomeSelected,
                incomeTitle = incomeAddState.title,
                incomeAmount = incomeAddState.amountString,
                incomeDate = incomeAddState.date.toString(),
                onAddIncome = onAddIncome,
                onIncomeTitleChange = onIncomeTitleChange,
                onIncomeAmountChange = onIncomeAmountChange,
                onShowIncomeDateBottomSheet = onShowIncomeDateBottomSheet,
                onRegularIncomeClick = onRegularIncomeClick,
                onVariableIncomeClick = onVariableIncomeClick,
            )
        }
    }
}

@Composable
private fun IncomeAddBody(
    regularIncomeSelected: Boolean,
    variableIncomeSelected: Boolean,
    incomeTitle: String,
    incomeAmount: String,
    incomeDate: String,
    onAddIncome: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onIncomeAmountChange: (String) -> Unit,
    onShowIncomeDateBottomSheet: () -> Unit,
    onRegularIncomeClick: () -> Unit,
    onVariableIncomeClick: () -> Unit,
) {
    Column {
        VerticalSpacer(20.dp)
        IncomeTitle("수입 타입")
        Row {
            IncomeTypeButton(
                text = "정기 수입",
                description = "매달 반복되는 수입",
                selected = regularIncomeSelected,
                onClick = onRegularIncomeClick,
                modifier = Modifier.weight(1f)
            )
            HorizontalSpacer(10.dp)
            IncomeTypeButton(
                text = "변동 수입",
                description = "매달 반복되지 않는 수입",
                selected = variableIncomeSelected,
                onClick = onVariableIncomeClick,
                modifier = Modifier.weight(1f)
            )
        }
        VerticalSpacer(30.dp)
        IncomeTitle("수입명")
        HmDefaultTextField(
            value = incomeTitle,
            onValueChange = onIncomeTitleChange,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            hint = "수입명을 입력해주세요"
        )
        VerticalSpacer(30.dp)
        IncomeTitle("수입 금액")
        HmDefaultTextField(
            value = incomeAmount,
            onValueChange = onIncomeAmountChange,
            hint = "수입 금액을 입력해주세요",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAddIncome()
                }
            )
        )
        VerticalSpacer(30.dp)
        IncomeTitle("수입 발생 날짜")
        NonTextField(
            text = incomeDate,
            onClick = onShowIncomeDateBottomSheet
        )
    }
}

@Composable
private fun IncomeTitle(title: String) {
    Text(
        text = title,
        style = JUNTheme.typography.titleMediumM,
    )
    VerticalSpacer(10.dp)
}

@Composable
private fun IncomeTypeButton(
    text: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textColor =
        if (selected) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.onSurface

    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray6),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceDim,
        onClick = onClick,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 30.dp)
        ) {
            Text(
                text = text,
                color = textColor,
                style = JUNTheme.typography.titleNormalB,
            )
            Text(
                text = description,
                color = textColor,
                style = JUNTheme.typography.labelLargeR,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IncomeModalContent(
    incomeModalEffect: IncomeModalEffect,
    onDateSelect: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
) {
    when (incomeModalEffect) {
        IncomeModalEffect.Idle -> {}
        is IncomeModalEffect.ShowDatePicker -> {
            DatePicker(
                onDateSelect = onDateSelect,
                timeBoundary = LocalDate.now().let { now -> YearMonth.from(now).atDay(1)..now },
                onDismissRequest = onDismissRequest,
            )
        }
    }
}

@Preview
@Composable
private fun IncomeAddScreenPreview() {
    JunTheme {
        IncomeAddScreen(
            title = "추가",
            incomeAddState = IncomeAddState.IncomeData(
                title = "월급",
                amount = 1000000.0,
                date = LocalDate.now(),
                type = IncomeType.REGULAR,
            ),
            onIncomeTitleChange = {},
            onIncomeAmountChange = {},
            onShowIncomeDateBottomSheet = {},
            onBackClick = {},
            onAddIncome = {},
            onRegularIncomeClick = {},
            onVariableIncomeClick = {},
        )
    }
}
