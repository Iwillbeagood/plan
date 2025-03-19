package jun.money.mate.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import jun.money.mate.res.R

@Composable
fun FlowerImage(
    size: Int,
    isAchieved: Boolean,
    modifier: Modifier = Modifier
) {
    Crossfade(
        targetState = isAchieved,
        label = "flower_animation",
        modifier = modifier
    ) { achieved ->
        Image(
            painter = painterResource(
                id = if (achieved) R.drawable.ic_smile_flower else R.drawable.ic_upset_flower
            ),
            contentDescription = "Flower Image",
            modifier = Modifier.size(size.dp)
        )
    }
}