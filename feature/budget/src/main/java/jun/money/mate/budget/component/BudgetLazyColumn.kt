package jun.money.mate.budget.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray7
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.consumption.Budget
import jun.money.mate.utils.currency.CurrencyFormatter

@Composable
internal fun BudgetItem(
    budget: Budget,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Gray7),
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
            Text(
                text = budget.title,
                style = TypoTheme.typography.titleNormalB,
                modifier = Modifier.padding(top = 10.dp)
            )
            VerticalSpacer(4.dp)
            Text(
                text = CurrencyFormatter.formatAmountWon(budget.amountUsed),
                style = TypoTheme.typography.headlineSmallM,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
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