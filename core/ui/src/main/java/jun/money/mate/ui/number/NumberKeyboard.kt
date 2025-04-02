package jun.money.mate.ui.number

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.BottomToTopAnimatedVisibility
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray10
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.main
import jun.money.mate.designsystem.theme.nonScaledSp

private enum class PriceButton(
    val price: Int,
    val title: String,
) {
    PRICE_1000(1000, "+1천"),
    PRICE_10000(10000, "+1만"),
    PRICE_100000(100000, "+10만"),
    PRICE_1000000(1000000, "+100만"),
}

@Composable
fun NumberKeyboard(
    visible: Boolean,
    buttonText: String = "닫기",
    onChangeNumber: (ValueState) -> Unit,
    onDismissRequest: () -> Unit,
) {
    BottomToTopAnimatedVisibility(
        visible = visible
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .shadow(10.dp, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .clickable(enabled = false) {}
                    .padding(vertical = 10.dp)
                    .align(Alignment.BottomCenter)
            ) {
                PriceButtonRow(
                    onChangeNumber = onChangeNumber,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                VerticalSpacer(8.dp)
                NumberField(
                    numberPadding = 4.dp,
                    isReset = true,
                    onChangeNumber = onChangeNumber
                )
                VerticalSpacer(8.dp)
                RegularButton(
                    text = buttonText,
                    color = main,
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun PriceButtonRow(
    onChangeNumber: (ValueState) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        PriceButton.entries.forEach { price ->
            RegularButton(
                text = price.title,
                color = Gray10,
                style = TypoTheme.typography.titleSmallM.nonScaledSp,
                isPreventMultipleClicks = false,
                onClick = {
                    onChangeNumber(ValueState.Plus(price.price))
                },
                verticalPadding = 4.dp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun NumberKeyboardPreview() {
    JunTheme {
        NumberKeyboard(
            visible = true,
            onChangeNumber = {},
            onDismissRequest = {}
        )
    }
}