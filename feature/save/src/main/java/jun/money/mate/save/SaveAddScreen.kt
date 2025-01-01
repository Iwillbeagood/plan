package jun.money.mate.save

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import jun.money.mate.designsystem.component.BottomToTopAnimatedVisibility
import jun.money.mate.designsystem.component.DefaultTextField
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.TextButton
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.UnderLineText
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Orange1
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SaveCategory
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.save.SaveAddState.Companion.buttonText
import jun.money.mate.save.component.SaveCategoryBottomSheet
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.number.NumberKeyboard
import jun.money.mate.ui.number.ValueState
import java.time.LocalDate

internal enum class SaveAddStep(
    val message: String
) {
    Category("먼저 저금 유형을 선택해 주세요"),
    Date("저금할 날짜를 선택해 주세요"),
    Title("어떤 저금인지 설명해 주세요"),
    Amount("저금할 금액을 입력해 주세요"),
    ;

    companion object {
        val startStep = Category
        val endStep = Amount

        fun SaveAddStep.nextStep(): SaveAddStep = entries.toTypedArray().let {
            val nextIndex = it.indexOf(this) + 1
            if (nextIndex < it.size) it[nextIndex] else it.last()
        }
    }
}

@Composable
internal fun SaveAddRoute(
    addType: AddType,
    onGoBack: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: SaveAddViewModel = hiltViewModel()
) {
    val saveAddState by viewModel.saveAddState.collectAsStateWithLifecycle()
    val saveModalEffect by viewModel.saveModalEffect.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    SaveAddScreen(
        title = when (addType) {
            is AddType.Edit -> "수정"
            AddType.New -> "추가"
        },
        saveAddState = saveAddState,
        onBackClick = onGoBack,
        onNextStep = viewModel::nextStep,
        onTitleValueChange = viewModel::onTitleValueChange,
        onShowDateBottomSheet = viewModel::showDatePicker,
        onShowCategoryBottomSheet = viewModel::showCategoryBottomSheet,
        onShowNumberBottomSheet = viewModel::showNumberKeyboard
    )

    SaveModalContent(
        saveModalEffect = saveModalEffect,
        onDateSelect = viewModel::onDateSelected,
        onDismissRequest = viewModel::dismiss,
        onCategorySelected = viewModel::categorySelected,
        onAmountChange = viewModel::amountValueChange,
        onNumberDismissRequest = viewModel::numberKeyboardDismiss,
    )

    LaunchedEffect(true) {
        viewModel.saveAddEffect.collect {
            when (it) {
                is SaveAddEffect.ShowSnackBar -> onShowSnackBar(it.messageType)
                SaveAddEffect.SaveAddComplete -> onGoBack()
                SaveAddEffect.DismissKeyboard -> keyboardController?.hide()
                SaveAddEffect.RemoveTextFocus -> focusManager.clearFocus()
            }
        }
    }
}

@Composable
private fun SaveAddScreen(
    title: String,
    saveAddState: SaveAddState,
    onBackClick: () -> Unit,
    onNextStep: () -> Unit,
    onTitleValueChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
    onShowNumberBottomSheet: () -> Unit,
) {
    AddScaffold(
        title = "저금 $title",
        buttonText = saveAddState.buttonText(),
        color = Orange1,
        onGoBack = onBackClick,
        onComplete = onNextStep,
    ) {
        SaveAddContent(
            saveAddState = saveAddState,
            onTitleValueChange = onTitleValueChange,
            onShowDateBottomSheet = onShowDateBottomSheet,
            onShowCategoryBottomSheet = onShowCategoryBottomSheet,
            onShowNumberBottomSheet = onShowNumberBottomSheet,
        )
    }
}

@Composable
private fun SaveAddContent(
    saveAddState: SaveAddState,
    onTitleValueChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
    onShowNumberBottomSheet: () -> Unit,
) {
    FadeAnimatedVisibility(saveAddState is SaveAddState.UiData) {
        if (saveAddState is SaveAddState.UiData) {
            SaveAddBody(
                uiState = saveAddState,
                onTitleChange = onTitleValueChange,
                onShowDateBottomSheet = onShowDateBottomSheet,
                onShowCategoryBottomSheet = onShowCategoryBottomSheet,
                onShowNumberBottomSheet = onShowNumberBottomSheet,
            )
        }
    }
}

@Composable
private fun SaveAddBody(
    uiState: SaveAddState.UiData,
    onTitleChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
    onShowNumberBottomSheet: () -> Unit,
) {
    val steps = uiState.steps
    val currentStep = uiState.currentStep
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .animateContentSize()
    ) {
        VerticalSpacer(50.dp)
        Text(
            text = uiState.currentStep.message,
            style = JUNTheme.typography.titleLargeM,
        )
        VerticalSpacer(20.dp)
        SaveAddStepColumn(
            step = SaveAddStep.Amount,
            steps = steps,
            currentStep = currentStep,
            title = "저금할 금액",
        ) {
            UnderLineText(
                value = uiState.amountString,
                hint = "선택",
                modifier = Modifier.clickable(onClick = onShowNumberBottomSheet),
            )
            TopToBottomAnimatedVisibility(uiState.amountWon.isNotEmpty()) {
                Text(
                    text = uiState.amountWon,
                    style = JUNTheme.typography.labelLargeM,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        SaveAddStepColumn(
            step = SaveAddStep.Title,
            steps = steps,
            currentStep = currentStep,
            title = "저금 계획명",
        ) {
            UnderLineText(
                value = uiState.title,
                hint = "저금 계획을 입력해주세요"
            )
        }
        SaveAddStepColumn(
            step = SaveAddStep.Date,
            steps = steps,
            currentStep = currentStep,
            title = "저금 날짜",
        ) {
            TextButton(
                text = "${uiState.day}일",
                onClick = onShowDateBottomSheet
            )
        }
        SaveAddStepColumn(
            step = SaveAddStep.Category,
            steps = steps,
            currentStep = currentStep,
            title = "저금 카테고리",
        ) {
            UnderLineText(
                value = uiState.category?.name ?: "카테고리를 선택해주세요",
                modifier = Modifier.clickable(onClick = onShowCategoryBottomSheet),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SaveModalContent(
    saveModalEffect: SaveModalEffect,
    onAmountChange: (ValueState) -> Unit,
    onDateSelect: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
    onCategorySelected: (SaveCategory) -> Unit,
    onNumberDismissRequest: () -> Unit,
) {
    when (saveModalEffect) {
        SaveModalEffect.Idle -> {}
        is SaveModalEffect.ShowDatePicker -> {
            DatePicker(
                onDateSelect = onDateSelect,
                onDismissRequest = onDismissRequest,
            )
        }
        SaveModalEffect.ShowCategoryBottomSheet -> {
            SaveCategoryBottomSheet(
                onDismiss = onDismissRequest,
                onCategorySelected = onCategorySelected
            )
        }
        SaveModalEffect.ShowNumberKeyboard -> {
            NumberKeyboard(
                visible = true,
                onChangeNumber = onAmountChange,
                onDismissRequest = onNumberDismissRequest,
            )
        }
    }
}

@Composable
private fun SaveAddStepColumn(
    step: SaveAddStep,
    steps: List<SaveAddStep>,
    currentStep: SaveAddStep,
    title: String,
    content: @Composable () -> Unit,
) {
    TopToBottomAnimatedVisibility(step in steps) {
        Column {
            BottomToTopAnimatedVisibility(currentStep != step) {
                Text(
                    text = title,
                    style = JUNTheme.typography.labelLargeM,
                )
            }
            content()
            VerticalSpacer(30.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveAddScreenPreview() {
    JunTheme {
        SaveAddScreen(
            title = "추가",
            saveAddState = SaveAddState.UiData(
                id = 0,
                title = "월급",
                amount = 1000000,
                category = SaveCategory.예금,
                day = 1,
            ),
            onTitleValueChange = {},
            onShowDateBottomSheet = {},
            onShowCategoryBottomSheet = {},
            onShowNumberBottomSheet = {},
            onBackClick = {},
            onNextStep = {}
        )
    }
}
