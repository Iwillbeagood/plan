package jun.money.mate.designsystem

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Gray3
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1

@Composable
fun Label(
    text: String,
    color: Color = Gray3,
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(6.dp),
    ) {
        Text(
            text = text,
            style = TypoTheme.typography.labelLargeM,
            color = White1,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
        )
    }
}

@Preview
@Composable
private fun LabelPreview() {
    JunTheme {
        Label(
            text = "Label",
            color = Gray3,
        )
    }
}
