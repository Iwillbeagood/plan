package jun.money.mate.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Gray1
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.LightBlue2


@Composable
fun DefaultTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    modifier : Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester(),
    isFocus: Boolean,
    hint: String = "",
    onDone: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    textFieldType: HmTextFieldType = HmTextFieldType.Default,
    textStyle: TextStyle = JUNTheme.typography.titleNormalR,
    onFocusChanged: (Boolean) -> Unit = {}
) {

    // HmTextFieldType에 따라 border color 결정
    val borderColor = when (textFieldType) {
        HmTextFieldType.Default -> if (isFocus) MaterialTheme.colorScheme.primary else Color.Transparent
        HmTextFieldType.Outlined -> if (isFocus) MaterialTheme.colorScheme.primary else LightBlue2
    }

    val shape = when (textFieldType) {
        HmTextFieldType.Default -> RectangleShape            // RoundedCorner 적용
        HmTextFieldType.Outlined -> RoundedCornerShape(4.dp) // RoundedCorner 미적용
    }

    // FocusRequester를 remember로 설정
//    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    LaunchedEffect(isFocus) {
        if (isFocus) {
            focusRequester.requestFocus()
        }
    }


    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                onFocusChanged(isFocus)
            },
        value = value,
        textStyle = textStyle,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        cursorBrush = SolidColor(if (isFocus) Color.Black else Color.Transparent) // 포커스가 없는 경우 커서도 투명하게 설정
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = shape
                )
                .padding(start = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    style = JUNTheme.typography.titleMediumR,
                    color = Gray1
                )
            }
            innerTextField()
        }
    }
}

sealed interface HmTextFieldType {
    data object Default : HmTextFieldType
    data object Outlined : HmTextFieldType
}



@Composable
@Preview(showBackground = true)
fun HmOutlinedTextFieldPreview() {
    Row(
        modifier = Modifier
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefaultTextField(
            value = "",
            isFocus = true,
            onValueChange = {},
            hint = "화물중량",
            textFieldType = HmTextFieldType.Outlined
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HmBasicTextFieldPreview() {
    Row(
        modifier = Modifier
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefaultTextField(
            value = "",
            onValueChange = {},
            hint = "화물중량",
            isFocus = false
        )
    }
}