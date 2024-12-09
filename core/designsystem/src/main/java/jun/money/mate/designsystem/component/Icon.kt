package jun.money.mate.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.main

@Composable
fun CircleIcon(
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = modifier
            .circleBackground(tint.copy(alpha = 0.4f))
            .size(size)
    )
}

@Preview
@Composable
private fun CircleIconPreview() {
    JunTheme {
        CircleIcon(
            icon = Icons.Default.Home,
            tint = main,
        )
    }
}