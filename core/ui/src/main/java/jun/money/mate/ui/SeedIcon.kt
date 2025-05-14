package jun.money.mate.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import jun.money.mate.res.R

@Composable
fun SeedIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(R.drawable.ic_acorn),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier,
    )
}
