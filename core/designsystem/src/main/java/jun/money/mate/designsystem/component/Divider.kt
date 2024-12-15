package jun.money.mate.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Gray8
import jun.money.mate.designsystem.theme.Gray9

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    lineColor: Color = Gray8,
    thickness: Dp = 1.dp
) {
    HorizontalDivider(
        modifier = modifier,
        color = lineColor,
        thickness = thickness
    )
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    lineColor: Color = Gray9,
    thickness: Dp = 1.dp
) {
    VerticalDivider(
        modifier = modifier,
        color = lineColor,
        thickness = thickness
    )
}

@Preview(showBackground = true)
@Composable
private fun HmHorizontalDividerPreview() {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        lineColor = Gray8
    )
}