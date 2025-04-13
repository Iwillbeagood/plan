package jun.money.mate.budget.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.DefaultBottomSheet
import jun.money.mate.designsystem.component.DefaultTextField
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.model.consumption.Used
import jun.money.mate.utils.currency.CurrencyFormatter
import java.time.LocalDate

enum class UsedAddSheetMode {
    ADD,
    EDIT,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UsedAddSheet(
    mode: UsedAddSheetMode,
    onDismissRequest: () -> Unit,
    onComplete: (Used) -> Unit,
    originUsed: Used? = null,
    sheetState: SheetState = rememberModalBottomSheetState(true),
) {
    var amount by remember { mutableStateOf(originUsed?.amount?.toString() ?: "") }
    var meno by remember { mutableStateOf(originUsed?.meno ?: "") }
    var date by remember { mutableStateOf(LocalDate.now()) }

    DefaultBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        sheetContent = {
            AddUsedSheetContent(
                amount = amount,
                meno = meno,
                buttonText = when (mode) {
                    UsedAddSheetMode.ADD -> "추가하기"
                    UsedAddSheetMode.EDIT -> "수정하기"
                },
                onMenoValueChange = { meno = it },
                onAmountValueChange = { amount = it },
                onDateSelected = { date = it },
                onComplete = {
                    onDismissRequest()
                    onComplete(
                        Used(
                            id = originUsed?.id ?: 0L,
                            meno = meno,
                            amount = amount.toLongOrNull() ?: 0L,
                            date = date,
                            budgetId = 0,
                        )
                    )
                }
            )
        }
    )
}

@Composable
private fun AddUsedSheetContent(
    meno: String,
    amount: String,
    buttonText: String,
    onMenoValueChange: (String) -> Unit,
    onAmountValueChange: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onComplete: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            Text(
                text = "사용 내역",
                style = TypoTheme.typography.headlineSmallB,
            )
            HorizontalSpacer(1f)
            Text(
                text = buttonText,
                style = TypoTheme.typography.titleNormalB,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .clickable(onClick = onComplete)
            )
        }
        VerticalSpacer(10.dp)
        AddUsedField(
            title = "메모"
        ) {
            Column {
                DefaultTextField(
                    value = meno,
                    onValueChange = onMenoValueChange,
                    hint = "메모를 입력해 주세요",
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    )
                )
            }
        }
        AddUsedField(
            title = "금액",
        ) {
            Column {
                DefaultTextField(
                    value = amount,
                    onValueChange = onAmountValueChange,
                    hint = "금액을 입력해 주세요",
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.NumberPassword
                    )
                )
                TopToBottomAnimatedVisibility(amount != "0") {
                    Column {
                        VerticalSpacer(4.dp)
                        Text(
                            text = CurrencyFormatter.formatAmountWon(amount),
                            style = TypoTheme.typography.labelLargeM,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        AddUsedField(
            title = "날짜",
        ) {
            DatePicker(
                onDateSelect = onDateSelected,
            )
        }
    }
}

@Composable
private fun AddUsedField(
    title: String,
    content: @Composable () -> Unit,
) {
    Column {
        Text(
            text = title,
            style = TypoTheme.typography.labelLargeM,
        )
        VerticalSpacer(8.dp)
        content()
        VerticalSpacer(10.dp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun UsedAddSheetPreview() {
    JunTheme {
        UsedAddSheet(
            sheetState = SheetState(true, Density(1f), SheetValue.Expanded, { true }, false),
            mode = UsedAddSheetMode.ADD,
            onDismissRequest = {},
            onComplete = {},
        )
    }
}