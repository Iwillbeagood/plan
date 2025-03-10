package jun.money.mate.save

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderLineText
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem_date.datetimepicker.DayPicker
import jun.money.mate.model.save.SavingsType
import jun.money.mate.save.component.SaveCategories
import jun.money.mate.save.contract.SaveAddEffect
import jun.money.mate.save.contract.SaveAddModalEffect
import jun.money.mate.save.contract.SaveAddState
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.number.NumberKeyboard
import jun.money.mate.ui.number.ValueState
import jun.money.mate.ui.interop.rememberPopBackStack
import jun.money.mate.ui.interop.rememberShowSnackBar

internal enum class SaveAddStep(
    val message: String
) {
    Category("먼저 무엇을 저축할지 선택해 주세요"),
    Amount("월 납입할 금액을 입력해 주세요"),
    Type("납입이 진행되는 날짜를 선택해 주세요");
}

@Composable
internal fun SaveAddRoute(
    viewModel: SaveAddViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.background)

    val showSnackBar = rememberShowSnackBar()
    val popBackStack = rememberPopBackStack()
    val saveAddState by viewModel.saveAddState.collectAsStateWithLifecycle()
    val saveModalEffect by viewModel.saveAddModalEffect.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    AddScaffold(
        buttonVisible = viewModel.addStep.value != SaveAddStep.Category,
        buttonText = when (viewModel.addStep.value) {
            SaveAddStep.Type -> "추가"
            else -> "다음"
        },
        onGoBack = popBackStack,
        onComplete = viewModel::nextStep,
    ) {
        SaveAddScreen(
            addStep = viewModel.addStep.value,
            addSteps = viewModel.addSteps.value,
            uiState = saveAddState,
            onShowNumberBottomSheet = viewModel::showNumberKeyboard,
            onDaySelected = viewModel::daySelected,
            onCategorySelected = viewModel::categorySelected,
            selectedCategory = saveAddState.category
        )
    }
    SaveModalContent(
        saveAddModalEffect = saveModalEffect,
        onAmountChange = viewModel::amountValueChange,
        onNumberDismissRequest = viewModel::numberKeyboardDismiss,
    )

    LaunchedEffect(true) {
        viewModel.saveAddEffect.collect {
            when (it) {
                is SaveAddEffect.ShowSnackBar -> showSnackBar(it.messageType)
                SaveAddEffect.SaveAddComplete -> popBackStack()
                SaveAddEffect.RemoveTextFocus -> focusManager.clearFocus()
            }
        }
    }
}

@Composable
private fun SaveAddScreen(
    addStep: SaveAddStep,
    addSteps: List<SaveAddStep>,
    uiState: SaveAddState,
    onCategorySelected: (SavingsType?) -> Unit,
    onShowNumberBottomSheet: () -> Unit,
    onDaySelected: (String) -> Unit,
    selectedCategory: SavingsType? = null,
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
            style = TypoTheme.typography.titleLargeM,
        )
        VerticalSpacer(40.dp)
        SaveAddField(
            visible = SaveAddStep.Type in addSteps,
            title = "납입날짜",
        ) {
            DayPicker(
                onDaySelected = onDaySelected,
                modifier = Modifier.fillMaxWidth()
            )
        }
        SaveAddField(
            visible = SaveAddStep.Amount in addSteps,
            title = "월 납입금액",
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
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        SaveAddField(
            visible = SaveAddStep.Category in addSteps,
            title = "카테고리",
        ) {
            SaveCategories(
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
            )
        }
        VerticalSpacer(20.dp)
    }
}

@Composable
private fun SaveAddField(
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
            VerticalSpacer(30.dp)
        }
    }
}

@Composable
private fun SaveModalContent(
    saveAddModalEffect: SaveAddModalEffect,
    onAmountChange: (ValueState) -> Unit,
    onNumberDismissRequest: () -> Unit,
) {
    when (saveAddModalEffect) {
        SaveAddModalEffect.Idle -> {}
        SaveAddModalEffect.ShowNumberKeyboard -> {
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
        SaveAddScreen(
            addStep = SaveAddStep.Amount,
            addSteps = SaveAddStep.entries,
            uiState = SaveAddState(),
            onCategorySelected = {},
            onShowNumberBottomSheet = {},
            onDaySelected = {},
        )
    }
}
