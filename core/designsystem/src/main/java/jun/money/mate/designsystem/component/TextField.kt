package jun.money.mate.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.icon.MyIconPack
import jun.money.mate.designsystem.icon.myiconpack.Visibility
import jun.money.mate.designsystem.icon.myiconpack.VisibilityOff
import jun.money.mate.designsystem.theme.Gray1
import jun.money.mate.designsystem.theme.Gray3
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme

@Composable
fun HmDefaultTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester(),
    value: String,
    hint: String = "",
    textStyle: TextStyle = JUNTheme.typography.titleMediumR,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit,
) {
    val requester by remember { mutableStateOf(focusRequester) }
    var isFocus by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        textStyle = textStyle,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
            .focusRequester(requester)
            .onFocusChanged {
                isFocus = it.isFocused
            }
    ) { innerTextField ->
        Surface(
            shape = RoundedCornerShape(5.dp),
            color = MaterialTheme.colorScheme.surfaceDim,
            border = BorderStroke(1.dp, Gray6),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (value.isEmpty() && !isFocus) {
                        Text(
                            text = hint,
                            style = textStyle
                        )
                    }
                    innerTextField()
                }
            }
        }
    }
}

@Composable
fun HmPasswordTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    value: String,
    hint: String = "",
    textStyle: TextStyle = JUNTheme.typography.titleMediumB,
    onValueChange: (String) -> Unit,
) {
    val requester by remember { mutableStateOf(focusRequester) }
    var isFocus by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
            .focusRequester(requester)
            .onFocusChanged {
                isFocus = it.isFocused
            }
    ) { innerTextField ->
        Surface(
            shape = RoundedCornerShape(5.dp),
            color = MaterialTheme.colorScheme.surfaceDim,
            border = BorderStroke(1.dp, Gray6),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (value.isEmpty() && !isFocus) {
                        Text(
                            text = hint,
                            style = textStyle
                        )
                    }
                    innerTextField()
                }

                val icon =
                    if (passwordVisibility) MyIconPack.Visibility else MyIconPack.VisibilityOff
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        passwordVisibility = !passwordVisibility
                    }
                )
            }
        }
    }
}

@Composable
fun NonTextField(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = JUNTheme.typography.titleMediumR,
) {
    Surface(
        shape = RoundedCornerShape(5.dp),
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceDim,
        border = BorderStroke(1.dp, Gray6),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = text,
                    style = textStyle,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun UnderlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = JUNTheme.typography.titleMediumB,
    hint: String = "",
    focusRequester: FocusRequester = FocusRequester(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val requester by remember { mutableStateOf(focusRequester) }
    var isFocus by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        maxLines = 1,
        modifier = modifier
            .focusRequester(requester)
            .onFocusChanged {
                isFocus = it.isFocused
            },
    ) { innerTextField ->
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (value.isEmpty() && !isFocus) {
                        Text(
                            text = hint,
                            style = textStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            }
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
private fun DefaultTextFieldPreview() {
    JunTheme() {
        HmDefaultTextField(
            value = "",
            onValueChange = {},
            hint = "인증번호 입력",
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun PasswordTextFieldPreview() {
    JunTheme {
        HmPasswordTextField(
            value = "a124124",
            onValueChange = {},
            hint = "인증번호 입력",
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun UnderlineTextFieldPreview() {
    JunTheme {
        UnderlineTextField(
            value = "",
            onValueChange = {},
            hint = "인증번호 입력",
            modifier = Modifier
        )
    }
}