package jun.money.mate.spending_plan.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.circleBackground
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingCategory.Companion.type
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingType

@Composable
internal fun PredictedSpendingItem(
    spendingPlan: SpendingPlan,
    onIncomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SpendingItemContainer(
        selected = spendingPlan.selected,
        onClick = onIncomeClick,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            CategoryIcon(
                category = spendingPlan.spendingCategory.type(),
                size = 25.dp,
                modifier = Modifier.circleBackground()
           )
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "-" + spendingPlan.amountString,
                    style = JUNTheme.typography.titleNormalB,
                    color = Red3,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = spendingPlan.titleString,
                    style = JUNTheme.typography.titleSmallR,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun PredictedSpendingItemPreview() {
    JunTheme {
        PredictedSpendingItem(
            spendingPlan = SpendingPlan(
                id = 1,
                title = "식비",
                amount = 10000,
                spendingCategory = SpendingCategory.ETC,
                planDay = 12,
                type = SpendingType.PredictedSpending,
                isApply = false,
            ),
            onIncomeClick = {},
        )
    }
}