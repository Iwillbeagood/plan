package jun.money.mate.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.etc.BooleanProvider
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.res.R

@Composable
fun LargeButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.btn_complete),
    color: Color = MaterialTheme.colorScheme.primary,
    inActiveColor: Color = Gray6,
    textColor: Color = White1,
    isActive: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) color else inActiveColor,
            contentColor = textColor,
            disabledContainerColor = inActiveColor
        ),
        contentPadding = PaddingValues(vertical = 14.dp),
        enabled = enabled,
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text, style = JUNTheme.typography.titleLargeB
        )
    }
}

@Composable
fun RegularButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.btn_complete),
    color: Color = Black,
    inActiveColor: Color = Gray6,
    textColor: Color = White1,
    isActive: Boolean = true,
    enabled: Boolean = true,
    borderStroke: Dp = 4.dp,
    onClick: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(borderStroke),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) color else inActiveColor,
            contentColor = textColor,
            disabledContainerColor = inActiveColor
        ),
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 4.dp),
        enabled = enabled,
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = JUNTheme.typography.titleNormalB,
            color = if (isActive) textColor else Black
        )
    }
}

@Preview
@Composable
fun LargeButtonPreview(
    @PreviewParameter(BooleanProvider::class) isActive: Boolean
) {
    JunTheme {
        LargeButton(
            onClick = {}
        )
    }
}

@Preview
@Composable
fun RegularButtonPreview(
    @PreviewParameter(BooleanProvider::class) isActive: Boolean
) {
    JunTheme {
        RegularButton(
            onClick = {}, isActive = isActive, modifier = Modifier.padding(16.dp)
        )
    }
}
