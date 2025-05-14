package jun.money.mate.ui.number

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.nonScaledSp

sealed interface ValueState {

    data class Substring(val newValue: Int) : ValueState
    data class Plus(val newValue: Int) : ValueState
    data object Remove : ValueState
    data object Reset : ValueState

    companion object {
        private const val MAX_VALUE = 100_000_000

        fun ValueState.passwordValue(originValue: String): String {
            return when (this) {
                is Substring -> {
                    originValue + newValue
                }
                is Plus -> {
                    originValue + newValue
                }
                Remove -> originValue.dropLast(1)
                Reset -> ""
            }
        }

        fun ValueState.value(originValue: String, maxValue: Int = MAX_VALUE): String {
            return when (this) {
                is Substring -> {
                    val combinedValue = if (originValue == "0") newValue.toString() else originValue + newValue
                    val combineInt = combinedValue.toIntOrNull() ?: originValue.toInt()
                    if (combineInt > maxValue) {
                        originValue
                    } else {
                        combinedValue
                    }
                }
                is Plus -> {
                    val originInt = originValue.toIntOrNull() ?: 0
                    val combineInt = newValue + originInt
                    if (combineInt > maxValue) {
                        originValue
                    } else {
                        combineInt.toString()
                    }
                }
                Remove -> originValue.dropLast(1).ifEmpty { "0" }
                Reset -> "0"
            }
        }
    }
}

@Composable
fun NumberField(
    onChangeNumber: (ValueState) -> Unit,
    modifier: Modifier = Modifier,
    isReset: Boolean = false,
    isBack: Boolean = true,
    numberPadding: Dp = 16.dp,
    numbers: List<Int> = (1..9).toList() + 0,
    style: TextStyle = TypoTheme.typography.headlineSmallB.nonScaledSp,
) {
    val randomNumbers = remember { numbers }

    val groups = listOf(
        randomNumbers.subList(0, 3),
        randomNumbers.subList(3, 6),
        randomNumbers.subList(6, 9),
    )
    val lastNumber = randomNumbers[9]

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        groups.forEach { group ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(4.dp),
            ) {
                group.forEach { number ->
                    NumberItem(
                        number = number,
                        style = style,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10))
                            .clickable {
                                onChangeNumber(ValueState.Substring(number))
                            }
                            .padding(numberPadding)
                            .weight(1f),
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (isReset) {
                Text(
                    text = "↻",
                    style = style,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10))
                        .clickable {
                            onChangeNumber(ValueState.Reset)
                        }
                        .padding(numberPadding)
                        .weight(1f),
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            NumberItem(
                number = lastNumber,
                style = style,
                modifier = Modifier
                    .clip(RoundedCornerShape(10))
                    .clickable {
                        onChangeNumber(ValueState.Substring(lastNumber))
                    }
                    .padding(numberPadding)
                    .weight(1f),
            )
            Box(
                modifier = Modifier.weight(1f),
            ) {
                FadeAnimatedVisibility(
                    visible = isBack,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "←",
                        style = style,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10))
                            .clickable {
                                onChangeNumber(ValueState.Remove)
                            }
                            .padding(numberPadding),
                    )
                }
            }
        }
        VerticalSpacer(10.dp)
    }
}

@Composable
private fun NumberItem(
    number: Int,
    style: TextStyle,
    modifier: Modifier,
) {
    Text(
        text = number.toString(),
        style = style,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun NumberFieldPreview() {
    JunTheme {
        NumberField(
            onChangeNumber = {},
            isReset = true,
            isBack = true,
        )
    }
}
