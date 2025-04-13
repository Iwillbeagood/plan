package jun.money.mate.budget.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import jun.money.mate.budget.navigation.NAV_NAME
import jun.money.mate.designsystem.component.DefaultBottomSheet
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditBudgetSheet(
    recommend: Long,
    onDismissRequest: () -> Unit,
    onEditBudget: (String) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(true),
) {
    val value = remember {
        mutableStateOf(
            if (recommend == 0L) "" else recommend.toString()
        )
    }

    DefaultBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        sheetTitle = "$NAV_NAME 변경",
        sheetContent = {
            BasicTextField(
                value = value.value,
                textStyle = TypoTheme.typography.titleMediumB.copy(color = MaterialTheme.colorScheme.onSurface),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onEditBudget(value.value)
                        onDismissRequest()
                    }
                ),
                onValueChange = {
                    value.value = it
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
            ) { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(BorderStroke(1.dp, Gray6), RoundedCornerShape(5.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    if (value.value.isEmpty()) {
                        Text(
                            text = "${NAV_NAME}을 입력하세요",
                            style = TypoTheme.typography.titleMediumR
                        )
                    }
                    innerTextField()
                }
            }
        },
        sheetButton1 = {
            RegularButton(
                text = "취소",
                onClick = onDismissRequest,
                isActive = false,
                modifier = Modifier.weight(1f)
            )
        },
        sheetButton2 = {
            RegularButton(
                text = "수정",
                onClick = {
                    onEditBudget(value.value)
                    onDismissRequest()
                },
                modifier = Modifier.weight(1f)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun EditSheetPreview() {
    JunTheme {
        EditBudgetSheet(
            sheetState = SheetState(true, Density(1f), SheetValue.Expanded, { true }, false),
            onDismissRequest = {},
            onEditBudget = {},
            recommend = 0L
        )
    }
}