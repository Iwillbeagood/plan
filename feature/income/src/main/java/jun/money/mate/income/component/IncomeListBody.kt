package jun.money.mate.income.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import jun.money.mate.designsystem.component.CheckBox
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.LeftToRightSlideFadeAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray5
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.main
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.income.IncomeType
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IncomeListBody(
    incomeList: IncomeList,
    onIncomeClick: (Income) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(30.dp),
                ) {
                     Text(
                        text = "전체 수입",
                        style = JUNTheme.typography.titleLargeM,
                    )
                    VerticalSpacer(10.dp)
                    Text(
                        text = incomeList.totalString,
                        color = main,
                        style = JUNTheme.typography.headlineSmallB,
                    )
                }
                HorizontalDivider(thickness = 5.dp)
            }
        }

        if (incomeList.regularIncomes.isNotEmpty()) {
            stickyHeader {
                IncomeStickyHeader(
                    title = IncomeType.REGULAR.title,
                )
            }
        }

        items(incomeList.regularIncomes) { income ->
            IncomeItem(
                income = income,
                onIncomeClick = { onIncomeClick(income) },
                modifier = Modifier.animateItem()
            )
        }

        if (incomeList.variableIncomes.isNotEmpty()) {
            stickyHeader {
                IncomeStickyHeader(
                    title = IncomeType.VARIABLE.title,
                )
            }
        }

        items(incomeList.variableIncomes) { income ->
            IncomeItem(
                income = income,
                onIncomeClick = { onIncomeClick(income) },
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
fun IncomeItem(
    income: Income,
    onIncomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (income.selected) main else Gray5
        ),
        color = MaterialTheme.colorScheme.surfaceDim,
        onClick = onIncomeClick,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            LeftToRightSlideFadeAnimatedVisibility(
                visible = income.selected
            ) {
                CheckBox(
                    checked = income.selected,
                    onCheckedChange = {
                        onIncomeClick()
                    },
                    modifier = Modifier.padding(end = 16.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = income.dateString,
                    style = JUNTheme.typography.titleSmallB,
                )
                Text(
                    text = income.title,
                    style = JUNTheme.typography.titleSmallR,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = income.amountString,
                    style = JUNTheme.typography.titleNormalB,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}

@Composable
private fun IncomeStickyHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp)
    ) {
        Text(
            text = title,
            style = JUNTheme.typography.titleNormalM,
        )
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
                type = IncomeType.REGULAR,
                incomeDate = LocalDate.now(),
                selected = true
            ),
            onIncomeClick = {}
        )
    }
}