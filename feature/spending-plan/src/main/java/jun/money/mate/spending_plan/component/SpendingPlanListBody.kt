package jun.money.mate.spending_plan.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.ScrollableTab
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.model.spending.ConsumptionSpend
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingPlanList
import jun.money.mate.model.spending.SpendingType

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SpendingPlanListBody(
    totalString: String,
    realTotalString: String,
    spendingPlanList: SpendingPlanList,
    consumptionSpend: List<ConsumptionSpend>,
    spendingTypeTabIndex: Int,
    onSpendingPlanClick: (SpendingPlan) -> Unit,
    onSpendingTabClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        item {
            Surface(
                color = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Row {
                        Text(
                            text = "실제 전체 지출",
                            style = JUNTheme.typography.titleLargeM,
                        )
                        Text(
                            text = realTotalString,
                            style = JUNTheme.typography.headlineSmallB,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row {
                        Text(
                            text = "예상 전체 지출",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = JUNTheme.typography.titleMediumM,
                        )
                        Text(
                            text = totalString,
                            style = JUNTheme.typography.titleNormalM,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        stickyHeader {
            ScrollableTab(
                tabs = SpendingType.entries.map { it.title },
                selectedTabIndex = spendingTypeTabIndex,
                onTabClick = onSpendingTabClick,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

        items(consumptionSpend) {
            ConsumptionPlanItem(
                consumptionSpend = it,
                onIncomeClick = { onSpendingPlanClick(it.spendingPlan) },
                modifier = Modifier.animateContentSize()
            )
        }

        spendingPlanList.predictPlanGroup.forEach { (date, spendingPlans) ->
            item {
                Text(
                    text = "매월 $date",
                    style = JUNTheme.typography.titleSmallR,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                )
            }

            items(spendingPlans) { spendingPlan ->
                PredictedSpendingItem(
                    spendingPlan = spendingPlan,
                    onIncomeClick = { onSpendingPlanClick(spendingPlan) },
                    modifier = Modifier.animateContentSize()
                )
            }
        }

        item {
            VerticalSpacer(100.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingPlanListBodyPreview() {
    JunTheme {
        SpendingPlanListBody(
            realTotalString = "50,000원",
            totalString = "100,000원",
            spendingPlanList = SpendingPlanList(
                spendingPlans = listOf(
                    SpendingPlan(
                        id = 3,
                        title = "식비",
                        amount = 10000,
                        spendingCategory = SpendingCategory.ETC,
                        planDay = 11,
                        type = SpendingType.PredictedSpending,
                        isApply = false,
                    ),
                    SpendingPlan(
                        id = 4,
                        title = "식비",
                        amount = 10000,
                        spendingCategory = SpendingCategory.ETC,
                        planDay = 11,
                        type = SpendingType.PredictedSpending,
                        isApply = false,
                    ),
                    SpendingPlan(
                        id = 5,
                        title = "식비",
                        amount = 10000,
                        spendingCategory = SpendingCategory.ETC,
                        planDay = 11,
                        type = SpendingType.PredictedSpending,
                        isApply = false,
                    ),
                    SpendingPlan(
                        id = 6,
                        title = "식비",
                        amount = 10000,
                        spendingCategory = SpendingCategory.ETC,
                        planDay = 11,
                        type = SpendingType.PredictedSpending,
                        isApply = false,
                    ),
                )
            ),
            consumptionSpend = listOf(
                ConsumptionSpend(
                    SpendingPlan(
                        id = 1,
                        title = "식비",
                        amount = 10000,
                        spendingCategory = SpendingCategory.ETC,
                        planDay = 11,
                        type = SpendingType.ConsumptionPlan,
                        isApply = false,
                    ),
                    10000,
                )
            ),
            spendingTypeTabIndex = 0,
            onSpendingPlanClick = {},
            onSpendingTabClick = {},
        )
    }
}