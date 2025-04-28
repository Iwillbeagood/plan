package jun.money.mate.budget.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Blue1
import jun.money.mate.designsystem.theme.Gray10
import jun.money.mate.designsystem.theme.Gray7
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.consumption.Budget
import jun.money.mate.utils.currency.CurrencyFormatter

internal fun LazyListScope.budgetList(
    budgets: List<Budget>,
    onShowDetail: (Long) -> Unit,
) {
    items(budgets) { used ->
        BudgetItem(
            budget = used,
            onClick = { onShowDetail(used.id) }
        )
    }
}

@Composable
private fun BudgetItem(
    budget: Budget,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Gray10,
        onClick = onClick,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            VerticalSpacer(10.dp)
            Text(
                text = budget.title,
                style = TypoTheme.typography.headlineSmallM,
            )
            VerticalSpacer(4.dp)
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = CurrencyFormatter.formatAmountWon(budget.budgetLeft),
                    style = TypoTheme.typography.titleNormalM,
                    color = Blue1
                )
                Text(
                    text = " 남았어요",
                    style = TypoTheme.typography.titleMediumR,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            VerticalSpacer(20.dp)
            BudgetChart(
                budget = budget.budget,
                amountUsed = budget.amountUsed,
            )
            VerticalSpacer(2.dp)
            Text(
                text = CurrencyFormatter.formatToWon(budget.budget),
                style = TypoTheme.typography.titleSmallB,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun BudgetLazyColumnPreview() {
    JunTheme {
        BudgetItem(
            Budget.sample,
            onClick = {}
        )
    }
}