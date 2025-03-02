package jun.money.mate.income.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import jun.money.mate.res.R

@Composable
fun LeafIcon(
    isRed: Boolean,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = if (isRed) R.drawable.ic_red_leaf else R.drawable.ic_leaf),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier
    )
}