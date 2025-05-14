package jun.money.mate.budget.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.consumption.Used
import jun.money.mate.utils.currency.CurrencyFormatter
import jun.money.mate.utils.formatDateBasedOnYear

internal fun LazyListScope.usedList(
    usedList: List<Used>,
    onClickUsed: (Used) -> Unit,
) {
    usedList
        .asSequence()
        .sortedByDescending { it.date }
        .groupBy { it.date }
        .forEach { (date, usedListBy) ->
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 2.dp,
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 16.dp),
                    ) {
                        Text(
                            text = formatDateBasedOnYear(date),
                            style = TypoTheme.typography.titleSmallM,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(),
                        )
                        VerticalSpacer(10.dp)

                        usedListBy.forEach { used ->
                            UsedItem(
                                used = used,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onClickUsed(used)
                                    }
                                    .padding(vertical = 4.dp),
                            )
                        }
                    }
                }
            }
        }
}

// 날짜, 제목, 금액
@Composable
private fun UsedItem(
    used: Used,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier,
    ) {
        Text(
            text = used.meno,
            style = TypoTheme.typography.titleMediumM,
        )
        HorizontalSpacer(1f)
        Text(
            text = "- " + CurrencyFormatter.formatAmountWon(used.amount),
            style = TypoTheme.typography.titleNormalB,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JunTheme {
        LazyColumn {
            usedList(
                listOf(Budget.usedSample),
                onClickUsed = {},
            )
        }
    }
}
