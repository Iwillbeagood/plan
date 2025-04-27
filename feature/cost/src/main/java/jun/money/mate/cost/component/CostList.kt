package jun.money.mate.cost.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Blue1
import jun.money.mate.designsystem.theme.Gray10
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.Gray9
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.LightBlue1
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.nonScaledSp
import jun.money.mate.model.Utils
import jun.money.mate.model.etc.DateType.Companion.toDateString
import jun.money.mate.model.spending.Cost
import jun.money.mate.model.spending.CostType
import jun.money.mate.model.spending.CostType.Companion.name
import jun.money.mate.res.R
import jun.money.mate.utils.toImageRes

@Composable
internal fun CostItem(
    cost: Cost,
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier
) {
    val border = if (cost.selected) {
        BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
    } else {
        null
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Gray10,
        shadowElevation = 2.dp,
        border = border,
        modifier = Modifier
            .height(90.dp)
            .padding(vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Icon(
                painter = painterResource(imageRes),
                tint = Color.Unspecified,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            HorizontalSpacer(30.dp)
            Column{
                Text(
                    text = Utils.formatAmountWon(cost.amount),
                    style = TypoTheme.typography.titleLargeB.nonScaledSp,
                )
                VerticalSpacer(4.dp)
                Text(
                    text = "${cost.day}일 | ${cost.costType.name}",
                    style = TypoTheme.typography.titleSmallM.nonScaledSp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            HorizontalSpacer(1f)
            Text(
                text = cost.daysRemainingFormatted,
                style = TypoTheme.typography.titleSmallM.nonScaledSp,
                color = Blue1,
            )
        }
    }
}

@Preview
@Composable
private fun CostListPreview() {
    JunTheme {
        CostItem(
            cost = Cost.samples.first(),
            imageRes = R.drawable.ic_coin,
            modifier = Modifier.fillMaxWidth()
        )
    }
}