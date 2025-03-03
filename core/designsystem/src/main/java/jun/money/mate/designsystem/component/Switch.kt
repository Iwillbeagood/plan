package jun.money.mate.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.etc.BooleanProvider
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.Gray5
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem.theme.main

@Composable
fun DefaultSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedColor: Color = main,
    uncheckedColor: Color = Gray5,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        Switch(
            checked = checked,
            colors = SwitchDefaults.colors(
                checkedThumbColor = checkedColor,
                uncheckedThumbColor = uncheckedColor,
                checkedTrackColor = White1,
                uncheckedTrackColor = White1,
                checkedBorderColor = checkedColor,
                uncheckedBorderColor = uncheckedColor
            ),
            thumbContent = {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = if (checked) checkedColor else uncheckedColor,
                        radius = 24f,
                        center = center
                    )
                }
            },
            onCheckedChange = onCheckedChange,
            modifier = modifier
        )
    }
}

@Composable
fun TextSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = TypoTheme.typography.titleNormalB,
    checkedColor: Color = main,
    uncheckedColor: Color = Gray5,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = text,
            color = if (checked) Black else Gray5,
            style = style
        )
        Spacer(modifier = Modifier.width(6.dp))
        DefaultSwitch(
            checked = checked,
            checkedColor = checkedColor,
            uncheckedColor = uncheckedColor,
            onCheckedChange = onCheckedChange
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun SwitchPreview(
    @PreviewParameter(BooleanProvider::class) checked: Boolean
) {
    JunTheme {
        Column {
            DefaultSwitch(
                checked = checked,
                onCheckedChange = {

                }
            )
        }
    }
}

@Preview
@Composable
private fun TextSwitchPreview(
    @PreviewParameter(BooleanProvider::class) checked: Boolean
) {
    JunTheme {
        Column {
            TextSwitch(
                text = "자동",
                checked = checked,
                onCheckedChange = {

                }
            )
        }
    }
}