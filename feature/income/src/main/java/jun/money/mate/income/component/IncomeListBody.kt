package jun.money.mate.income.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import jun.money.mate.designsystem.component.CheckIcon
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem_date.datetimepicker.MonthBar
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.etc.DateType.Companion.toDateString
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import jun.money.mate.ui.LeafIcon
import java.time.LocalDate

@Composable
internal fun IncomeListBody(
    incomeList: IncomeList,
    onIncomeClick: (Income) -> Unit,
    modifier : Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.surfaceDim,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            VerticalSpacer(20.dp)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    VerticalSpacer(10.dp)
                }
                items(incomeList.incomes) { income ->
                    IncomeItem(
                        income = income,
                        onIncomeClick = { onIncomeClick(income) },
                        modifier = Modifier.animateContentSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun IncomeItem(
    income: Income,
    onIncomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceDim,
        onClick = onIncomeClick,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (income.isSelected) {
                        CheckIcon()
                    } else {
                        LeafIcon(
                            isRed = income.dateType is DateType.Specific,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    HorizontalSpacer(6.dp)
                    Column {
                        Text(
                            text = income.title,
                            style = TypoTheme.typography.titleMediumM,
                        )
                        Text(
                            text = income.dateType.toDateString(),
                            style = TypoTheme.typography.titleSmallR,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Text(
                text = income.amountString,
                style = TypoTheme.typography.titleNormalB,
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview
@Composable
private fun IncomeListBodyPreview() {
    JunTheme {
        IncomeListBody(
            incomeList = IncomeList(
                incomes = listOf(
                    Income.regularSample,
                    Income.variableSample,
                ),
            ),
            onIncomeClick = {}
        )
    }
}

@Preview
@Composable
private fun IncomeItemPreview() {
    JunTheme {
        IncomeItem(
            income = Income(
                id = 1,
                title = "Title",
                amount = 1000,
                dateType = DateType.Monthly(1),
            ),
            onIncomeClick = {}
        )
    }
}