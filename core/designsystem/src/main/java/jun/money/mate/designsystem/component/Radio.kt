package jun.money.mate.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.etc.BooleanProvider
import jun.money.mate.designsystem.etc.ComponentItem
import jun.money.mate.designsystem.etc.ComponentItem.Companion.toComponentItems
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem.theme.main

@Composable
fun RadioGroup(
    modifier: Modifier = Modifier,
    radioList: List<ComponentItem>,
    orientation: Orientation,
    onClick: (ComponentItem) -> Unit,
    verticalSpace: Dp = 10.dp,
) {
    var list by remember { mutableStateOf(radioList) }

    val onRadioClick: (ComponentItem) -> Unit = { selectRadio ->
        list = list.map {
            it.copy(selected = it.text == selectRadio.text)
        }
        onClick(selectRadio)
    }

    if (orientation == Orientation.Vertical) {
        Column(
            verticalArrangement = Arrangement.spacedBy(verticalSpace),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
        ) {
            Radios(
                list = list,
                onRadioClick = onRadioClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {
            Radios(
                list = list,
                onRadioClick = onRadioClick,
            )
        }
    }
}

@Composable
private fun Radios(
    list: List<ComponentItem>,
    onRadioClick: (ComponentItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    list.forEach {
        RadioButton(
            modifier = modifier,
            text = it.text,
            selected = it.selected,
            onClick = {
                onRadioClick(it)
            },
        )
    }
}

@Composable
fun RadioButton(
    modifier: Modifier = Modifier,
    text: String = "",
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                onClick()
            },
    ) {
        Canvas(
            modifier = Modifier.size(26.dp),
        ) {
            drawCircle(
                color = main,
                radius = 40f,
                center = center,
            )
            drawCircle(
                color = White1,
                radius = 35f,
                center = center,
            )
            drawCircle(
                color = if (selected) main else White1,
                radius = 22f,
                center = center,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = if (selected) TypoTheme.typography.titleNormalM else TypoTheme.typography.titleNormalR,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioButtonPreview(
    @PreviewParameter(BooleanProvider::class) isSelected: Boolean,
) {
    JunTheme {
        RadioButton(
            text = "자동",
            selected = isSelected,
        ) {}
    }
}

@Preview
@Composable
private fun RadioGroupVerticalPreview() {
    JunTheme {
        RadioGroup(
            radioList = listOf("자동", "주간", "야간").toComponentItems(),
            orientation = Orientation.Vertical,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun RadioGroupHorizontalPreview() {
    JunTheme {
        RadioGroup(
            radioList = listOf("자동", "주간", "야간").toComponentItems(),
            orientation = Orientation.Horizontal,
            onClick = {},
        )
    }
}
