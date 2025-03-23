package jun.money.mate.spending_plan.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.Utils
import jun.money.mate.model.etc.DateType.Companion.date
import jun.money.mate.model.etc.DateType.Companion.toDateString
import jun.money.mate.model.spending.Cost
import jun.money.mate.model.spending.CostType
import jun.money.mate.model.spending.CostType.Companion.name
import jun.money.mate.res.R
import jun.money.mate.utils.toImageRes

@Composable
internal fun CostList(
    costs: List<Cost>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            HorizontalSpacer(6.dp)
        }
        items(costs) { cost ->
            CostItem(
                cost = cost,
                imageRes = when (val type = cost.costType) {
                    is CostType.Subscription -> type.type.toImageRes()
                    else -> R.drawable.ic_coin
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {

                    }
                    .padding(vertical = 20.dp, horizontal = 30.dp)
            )
        }
        item {
            HorizontalSpacer(6.dp)
        }
    }
}

@Composable
private fun CostItem(
    cost: Cost,
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, Gray6),
        modifier = Modifier
            .padding(4.dp)
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
            HorizontalSpacer(16.dp)
            Column{
                Text(
                    text = Utils.formatAmountWon(cost.amount),
                    style = TypoTheme.typography.headlineSmallB,
                )
                VerticalSpacer(4.dp)
                Text(
                    text = cost.costType.name + " | " + cost.dateType.toDateString(),
                    style = TypoTheme.typography.titleMediumM,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            HorizontalSpacer(10.dp)
        }
    }
}

@Preview
@Composable
private fun CostListPreview() {
    JunTheme {
        CostList(
            costs = Cost.samples
        )
    }
}