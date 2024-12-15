package jun.money.mate.save

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.DefaultTextField
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.TextButton
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Orange1
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SaveCategory
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.save.component.SaveCategoryBottomSheet
import jun.money.mate.ui.AddScaffold
import jun.money.mate.ui.AddTitleContent
import java.time.LocalDate

@Composable
internal fun SaveAddRoute(
    addType: AddType,
    onGoBack: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: SaveAddViewModel = hiltViewModel()
) {
    val saveAddState by viewModel.saveAddState.collectAsStateWithLifecycle()
    val saveModalEffect by viewModel.saveModalEffect.collectAsStateWithLifecycle()
    val listState = rememberScrollState()

    SaveAddScreen(
        listState = listState,
        title = when (addType) {
            is AddType.Edit -> "수정"
            AddType.New -> "추가"
        },
        saveAddState = saveAddState,
        onBackClick = onGoBack,
        onAddSave = viewModel::onAddSave,
        onTitleValueChange = viewModel::onTitleValueChange,
        onAmountValueChange = viewModel::onAmountValueChange,
        onShowDateBottomSheet = viewModel::showDatePicker,
        onShowCategoryBottomSheet = viewModel::showCategoryBottomSheet,
    )

    IncomeModalContent(
        saveModalEffect = saveModalEffect,
        onDateSelect = viewModel::onDateSelected,
        onDismissRequest = viewModel::onDismiss,
        onCategorySelected = viewModel::categorySelected,
    )

    LaunchedEffect(true) {
        viewModel.saveAddEffect.collect {
            when (it) {
                is SaveAddEffect.ShowSnackBar -> onShowSnackBar(it.messageType)
                SaveAddEffect.SaveAddComplete -> onGoBack()
                SaveAddEffect.ScrollToBottom -> {
                    listState.animateScrollTo(listState.maxValue)
                }
            }
        }
    }
}

@Composable
private fun SaveAddScreen(
    listState: ScrollState,
    title: String,
    saveAddState: SaveAddState,
    onBackClick: () -> Unit,
    onAddSave: () -> Unit,
    onTitleValueChange: (String) -> Unit,
    onAmountValueChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
) {
    AddScaffold(
        title = "저금 $title",
        color = Orange1,
        onGoBack = onBackClick,
        onComplete = onAddSave,
    ) {
        SaveAddContent(
            listState = listState,
            saveAddState = saveAddState,
            onTitleValueChange = onTitleValueChange,
            onAmountValueChange = onAmountValueChange,
            onShowDateBottomSheet = onShowDateBottomSheet,
            onShowCategoryBottomSheet = onShowCategoryBottomSheet,
        )
    }
}

@Composable
private fun SaveAddContent(
    listState: ScrollState,
    saveAddState: SaveAddState,
    onTitleValueChange: (String) -> Unit,
    onAmountValueChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
) {
    FadeAnimatedVisibility(saveAddState is SaveAddState.SaveData) {
        if (saveAddState is SaveAddState.SaveData) {
            SaveAddBody(
                listState = listState,
                saveCategory = saveAddState.category,
                title = saveAddState.title,
                amount = saveAddState.amountString,
                amountWon = saveAddState.amountWon,
                date = saveAddState.day.toString(),
                onTitleChange = onTitleValueChange,
                onAmountChange = onAmountValueChange,
                onShowDateBottomSheet = onShowDateBottomSheet,
                onShowCategoryBottomSheet = onShowCategoryBottomSheet,
            )
        }
    }
}

@Composable
private fun SaveAddBody(
    listState: ScrollState,
    saveCategory: SaveCategory?,
    title: String,
    amount: String,
    amountWon: String,
    date: String,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
) {
    val focusRequester1 = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .verticalScroll(listState)
            .animateContentSize()
    ) {
        AddTitleContent(
            "저금 카테고리",
        ) {
            TextButton(
                text = saveCategory?.name ?: "카테고리를 선택해주세요",
                onClick = onShowCategoryBottomSheet
            )
        }
        AddTitleContent("저금 날짜") {
            TextButton(
                text = "${date}일",
                onClick = onShowDateBottomSheet
            )
        }
        AddTitleContent("저금 계획명") {
            DefaultTextField(
                value = title,
                onValueChange = onTitleChange,
                focusRequester = focusRequester1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                hint = "저금 계획을 입력해주세요"
            )
        }
        AddTitleContent("매월 저금할 금액") {
            DefaultTextField(
                value = amount,
                onValueChange = onAmountChange,
                hint = "저금 금액을 입력해주세요",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.NumberPassword
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onShowCategoryBottomSheet()
                    }
                )
            )
            TopToBottomAnimatedVisibility(amountWon.isNotEmpty()) {
                Text(
                    text = amountWon,
                    style = JUNTheme.typography.labelLargeM,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        VerticalSpacer(30.dp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IncomeModalContent(
    saveModalEffect: SaveModalEffect,
    onDateSelect: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
    onCategorySelected: (SaveCategory) -> Unit,
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
    }
}

@Preview(showBackground = true)
@Composable
private fun IncomeAddScreenPreview() {
    JunTheme {
        SaveAddScreen(
            listState = rememberScrollState(),
            title = "추가",
            saveAddState = SaveAddState.SaveData(
                id = 0,
                title = "월급",
                amount = 1000000,
                category = SaveCategory.예금,
                day = 1,
            ),
            onTitleValueChange = {},
            onAmountValueChange = {},
            onShowDateBottomSheet = {},
            onShowCategoryBottomSheet = {},
            onBackClick = {},
            onAddSave = {},
        )
    }
}
