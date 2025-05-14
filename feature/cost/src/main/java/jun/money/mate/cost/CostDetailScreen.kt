package jun.money.mate.cost

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.cost.component.CostTypeSelector
import jun.money.mate.cost.contract.CostDetailEffect
import jun.money.mate.cost.contract.CostDetailState
import jun.money.mate.cost.navigation.Title
import jun.money.mate.designsystem.component.StateAnimatedVisibility
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderlineTextField
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem_date.datetimepicker.DayPicker
import jun.money.mate.model.spending.CostType
import jun.money.mate.model.spending.NormalType
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.ui.AddScaffold

@Composable
internal fun CostDetailRoute(
    viewModel: CostDetailViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

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
    StateAnimatedVisibility<CostDetailState.UiData>(
        target = uiState,
    ) {
        ChallengeScreen(
            uiState = it,
            onCostTypeSelected = viewModel::costTypeSelected,
            onAmountValueChange = viewModel::amountValueChange,
            onDaySelected = viewModel::daySelected,
        )
    }
}

@Composable
private fun ChallengeScreen(
    uiState: CostDetailState.UiData,
    onCostTypeSelected: (CostType?) -> Unit,
    onAmountValueChange: (String) -> Unit,
    onDaySelected: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .animateContentSize()
    ) {
        VerticalSpacer(50.dp)
        ChallengeDetailField(
            title = "$Title 유형",
        ) {
            CostTypeSelector(
                onSelected = onCostTypeSelected,
                costType = uiState.costType
            )
        }
        ChallengeDetailField(
            title = "$Title 날짜",
        ) {
            DayPicker(
                onDaySelected = onDaySelected,
                selectedDay = uiState.day.toString(),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        ChallengeDetailField(
            title = "$Title 금액",
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
                day = 1,
                amount = 10000L,
            ),
            onCostTypeSelected = { },
            onAmountValueChange = { },
            onDaySelected = { },
        )
    }
}