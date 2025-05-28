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
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystemDate.datetimepicker.MonthBar
import jun.money.mate.model.Utils.formatDateToKorean
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.etc.DateType.Monthly
import jun.money.mate.model.etc.DateType.Specific
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import jun.money.mate.ui.LeafIcon
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun IncomeListBody(
    month: YearMonth,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    incomeList: IncomeList,
    onIncomeClick: (Income) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            VerticalSpacer(20.dp)
            MonthBar(
                month = month,
                onPrev = onPrev,
                onNext = onNext,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            VerticalSpacer(20.dp)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    VerticalSpacer(10.dp)
                }
                items(incomeList.incomes) { income ->
                    IncomeItem(
                        income = income,
                        onIncomeClick = { onIncomeClick(income) },
                        modifier = Modifier.animateContentSize(),
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
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
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
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (income.isSelected) {
                        CheckIcon()
                    } else {
                        LeafIcon(
                            isRed = income.dateType == DateType.Specific,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                    HorizontalSpacer(6.dp)
                    Column {
                        Text(
                            text = income.title,
                            style = TypoTheme.typography.titleMediumM,
                            maxLines = 1,
                        )
                        Text(
                            text =
                            when (income.dateType) {
                                Monthly -> "매월 ${income.date}일"
                                Specific -> formatDateToKorean(income.localDate)
                            },
                            style = TypoTheme.typography.titleSmallR,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Text(
                text = income.amountString,
                style = TypoTheme.typography.titleNormalB,
                textAlign = TextAlign.End,
            )
        }
    }
}

@Preview
@Composable
private fun IncomeListBodyPreview() {
    JunTheme {
        IncomeListBody(
            month = YearMonth.now(),
            onPrev = {},
            onNext = {},
            incomeList = IncomeList(
                incomes = listOf(
                    Income.regularSample,
                    Income.variableSample,
                ),
            ),
            onIncomeClick = {},
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
                date = 1,
                addDate = LocalDate.now(),
                dateType = Monthly,
            ),
            onIncomeClick = {},
        )
    }
}
