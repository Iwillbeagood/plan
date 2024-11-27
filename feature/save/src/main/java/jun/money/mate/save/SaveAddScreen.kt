package jun.money.mate.save

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import jun.money.mate.designsystem.component.DefaultTextField
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.TextButton
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Orange1
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SaveCategory
import jun.money.mate.model.save.SaveType
import jun.money.mate.navigation.argument.AddType
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

    SaveAddScreen(
        title = when (addType) {
            is AddType.Edit -> "수정"
            AddType.New -> "추가"
        },
        saveAddState = saveAddState,
        onBackClick = onGoBack,
        onAddSave = viewModel::onAddSave,
        onTitleValueChange = viewModel::onTitleValueChange,
        onAmountValueChange = viewModel::onAmountValueChange,
        onShowDateBottomSheet = viewModel::onShowDatePicker,
        onShowCategoryBottomSheet = viewModel::showCategoryBottomSheet,
        onApplyType = viewModel::onSaveTypeSelect,
    )

    IncomeModalContent(
        saveModalEffect = saveModalEffect,
        onDateSelect = viewModel::onDateSelected,
        onDismissRequest = viewModel::onDismiss
    )

    LaunchedEffect(true) {
        viewModel.saveAddEffect.collect {
            when (it) {
                is SaveAddEffect.ShowSnackBar -> onShowSnackBar(it.messageType)
                SaveAddEffect.SaveAddComplete -> onGoBack()
            }
        }
    }
}

@Composable
private fun SaveAddScreen(
    title: String,
    saveAddState: SaveAddState,
    onBackClick: () -> Unit,
    onAddSave: () -> Unit,
    onTitleValueChange: (String) -> Unit,
    onAmountValueChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
    onApplyType: (SaveType) -> Unit,
) {
    AddScaffold(
        title = "저금 $title",
        color = Orange1,
        onGoBack = onBackClick,
        onComplete = onAddSave,
    ) {
        SaveAddContent(
            saveAddState = saveAddState,
            onAddIncome = onAddSave,
            onIncomeTitleChange = onTitleValueChange,
            onIncomeAmountChange = onAmountValueChange,
            onShowDateBottomSheet = onShowDateBottomSheet,
            onShowCategoryBottomSheet = onShowCategoryBottomSheet,
            onApplyType = onApplyType,
        )
    }
}

@Composable
private fun SaveAddContent(
    saveAddState: SaveAddState,
    onAddIncome: () -> Unit,
    onIncomeTitleChange: (String) -> Unit,
    onIncomeAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
    onApplyType: (SaveType) -> Unit,
) {
    FadeAnimatedVisibility(saveAddState is SaveAddState.SaveData) {
        if (saveAddState is SaveAddState.SaveData) {
            SaveAddBody(
                type = saveAddState.type,
                saveCategory = saveAddState.category,
                title = saveAddState.title,
                amount = saveAddState.amountString,
                amountWon = saveAddState.amountWon,
                date = saveAddState.day.toString(),
                onAdd = onAddIncome,
                onTitleChange = onIncomeTitleChange,
                onAmountChange = onIncomeAmountChange,
                onShowDateBottomSheet = onShowDateBottomSheet,
                onShowCategoryBottomSheet = onShowCategoryBottomSheet,
                onApplyType = onApplyType,
            )
        }
    }
}

@Composable
private fun SaveAddBody(
    type: SaveType,
    saveCategory: SaveCategory?,
    title: String,
    amount: String,
    amountWon: String,
    date: String,
    onAdd: () -> Unit,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onShowDateBottomSheet: () -> Unit,
    onShowCategoryBottomSheet: () -> Unit,
    onApplyType: (SaveType) -> Unit,
) {
    val listState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(listState)
            .animateContentSize()
    ) {
        AddTitleContent("지출 카테고리") {
            Row {
                SaveTypeButton(
                    type = SaveType.PlaningSave,
                    hint = "목표를 정하고 저축 계획을 세웁니다.",
                    isType = type == SaveType.PlaningSave,
                    onApplyType = onApplyType,
                    modifier = Modifier.weight(1f)
                )
                HorizontalSpacer(10.dp)
                SaveTypeButton(
                    type = SaveType.ContinueSave,
                    hint = "기한이 없는 지속적인 저축을 계획합니다.",
                    isType = type == SaveType.ContinueSave,
                    onApplyType = onApplyType,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        AddTitleContent("저금 계획명") {
            DefaultTextField(
                value = title,
                onValueChange = onTitleChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                hint = "저금 계획을 입력해주세요"
            )
        }
        AddTitleContent("저금 금액") {
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
                        onAdd()
                    }
                )
            )
            Text(
                text = amountWon,
                style = JUNTheme.typography.labelLargeM,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
        AddTitleContent(
            "저금 카테고리",
            visible = type == SaveType.ContinueSave
        ) {
            TextButton(
                text = saveCategory?.name ?: "카테고리를 선택해주세요",
                onClick = onShowCategoryBottomSheet
            )
        }
        AddTitleContent("저금 발생 날짜") {
            TextButton(
                text = "${date}일",
                onClick = onShowDateBottomSheet
            )
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
) {
    when (saveModalEffect) {
        SaveModalEffect.Idle -> {}
        is SaveModalEffect.ShowDatePicker -> {
            DatePicker(
                onDateSelect = onDateSelect,
                onDismissRequest = onDismissRequest,
            )
        }

        SaveModalEffect.ShowCategoryBottomSheet -> TODO()
    }
}
@Composable
private fun SaveTypeButton(
    type: SaveType,
    hint: String,
    isType: Boolean,
    onApplyType: (SaveType) -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        text = type.title,
        hint = hint,
        onClick = { onApplyType(type) },
        color = if (isType) Orange1 else MaterialTheme.colorScheme.surfaceDim,
        textColor = if (isType) White1 else MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun IncomeAddScreenPreview() {
    JunTheme {
        SaveAddScreen(
            title = "추가",
            saveAddState = SaveAddState.SaveData(
                id = 0,
                title = "월급",
                amount = 1000000,
                type = SaveType.PlaningSave,
                category = SaveCategory.예금,
                day = 1,
            ),
            onTitleValueChange = {},
            onAmountValueChange = {},
            onShowDateBottomSheet = {},
            onShowCategoryBottomSheet = {},
            onBackClick = {},
            onAddSave = {},
            onApplyType = {}
        )
    }
}
