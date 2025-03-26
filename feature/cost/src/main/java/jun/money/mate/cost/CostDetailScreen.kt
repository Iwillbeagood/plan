package jun.money.mate.cost

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.cost.component.CostTypeSelector
import jun.money.mate.cost.contract.CostDetailEffect
import jun.money.mate.cost.contract.CostDetailState
import jun.money.mate.cost.contract.CostEffect
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.spending.CostType
import jun.money.mate.model.spending.NormalType
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.DateAdd
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun CostDetailRoute(
    viewModel: CostDetailViewModel = hiltViewModel()
) {
    ChangeStatusBarColor()

    val navigateAction = LocalNavigateActionInterop.current
    val showSnackBar = rememberShowSnackBar()
    val uiState by viewModel.costDetailState.collectAsStateWithLifecycle()

    AddScaffold(
        buttonText = "수정하기",
        onGoBack = navigateAction::popBackStack,
        onComplete = viewModel::editCost
    ) {
        ChallengeContent(
            uiState = uiState,
            viewModel = viewModel
        )
    }

    LaunchedEffect(Unit) {
        viewModel.costEffect.collect { effect ->
            when (effect) {
                is CostDetailEffect.ShowSnackBar -> showSnackBar(effect.messageType)
                CostDetailEffect.EditComplete -> navigateAction.popBackStack()
            }
        }
    }
}

@Composable
private fun ChallengeContent(
    uiState: CostDetailState,
    viewModel: CostDetailViewModel
) {
    FadeAnimatedVisibility(
        uiState is CostDetailState.UiData,
    ) {
        if (uiState is CostDetailState.UiData) {
            ChallengeScreen(
                uiState = uiState,
                onCostTypeSelected = viewModel::costTypeSelected,
                onAmountValueChange = viewModel::amountValueChange,
                onDaySelected = viewModel::daySelected,
                onDateSelected = viewModel::dateSelected,
            )
        }
    }
}

@Composable
private fun ChallengeScreen(
    uiState: CostDetailState.UiData,
    onCostTypeSelected: (CostType?) -> Unit,
    onAmountValueChange: (String) -> Unit,
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
        ChallengeDetailField(
            title = "소비 유형",
        ) {
            CostTypeSelector(
                onSelected = onCostTypeSelected,
                costType = uiState.costType
            )
        }
        ChallengeDetailField(
            title = "소비 날짜",
        ) {
            DateAdd(
                type = "소비",
                onDaySelected = onDaySelected,
                onDateSelected = onDateSelected,
                originDateType = uiState.dateType
            )
        }
        ChallengeDetailField(
            title = "소비 금액",
        ) {
            UnderlineTextField(
                value = uiState.amountString,
                onValueChange = onAmountValueChange,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                hint = "금액을 입력해 주세요"
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
}

@Composable
private fun ChallengeDetailField(
    title: String,
    content: @Composable () -> Unit,
) {
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

@Preview(showBackground = true)
@Composable
private fun SpendingListScreenPreview() {
    JunTheme {
        ChallengeScreen(
            uiState = CostDetailState.UiData(
                id = 0,
                costType = CostType.Normal(NormalType.교통비),
                dateType = DateType.Monthly(1, YearMonth.now()),
                amount = 10000L,
            ),
            onCostTypeSelected = { },
            onAmountValueChange = { },
            onDaySelected = { },
            onDateSelected = { },
        )
    }
}