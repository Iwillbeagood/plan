package jun.money.mate.challenge.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.DefaultTextField
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme

@Composable
internal fun AmountOrCount(
    amountValue: String,
    onAmountValueChange: (String) -> Unit,
    countValue: String,
    onCountValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        TextBox(
            value = amountValue,
            onValueChange = onAmountValueChange,
            label = "1회 납입 금액",
            unit = "원",
            modifier = modifier,
        )
        Text(
            text = "=",
            style = TypoTheme.typography.displayLargeB,
        )
        TextBox(
            value = countValue,
            onValueChange = onCountValueChange,
            label = "횟수",
            unit = "회",
            modifier = modifier,
        )
    }
}

@Composable
private fun TextBox(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    unit: String,
    modifier: Modifier = Modifier,
) {
    DefaultTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        textStyle = TypoTheme.typography.titleNormalR,
        textAlign = TextAlign.End,
        unit = unit,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun AmountOrCountPreview() {
    JunTheme {
        AmountOrCount(
            amountValue = "30",
            onAmountValueChange = {},
            countValue = "3",
            onCountValueChange = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
