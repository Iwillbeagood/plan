package jun.money.mate.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import jun.money.mate.res.R

@Composable
fun LeafIcon(
    modifier: Modifier = Modifier,
    isRed: Boolean = false,
) {
    Icon(
        painter = painterResource(id = if (isRed) R.drawable.ic_red_leaf else R.drawable.ic_leaf),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier,
    )
}
