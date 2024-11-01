package jun.money.mate.spending_plan.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.CheckBox
import jun.money.mate.designsystem.component.LeftToRightSlideFadeAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray5
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem.theme.main
import jun.money.mate.model.income.Income
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingPlanList
import kotlin.collections.component1
import kotlin.collections.component2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpendingPlanListBody(
    spendingPlanList: SpendingPlanList,
    onSpendingPlanClick: (SpendingPlan) -> Unit
) {
    Column {
        Surface(
            color = MaterialTheme.colorScheme.surfaceDim,
            shadowElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
            ) {
                Text(
                    text = "전체 지출",
                    style = JUNTheme.typography.titleLargeM,
                )
                VerticalSpacer(10.dp)
                Text(
                    text = spendingPlanList.totalString,
                    color = Red3,
                    style = JUNTheme.typography.headlineSmallB,
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceDim)
        ) {
            spendingPlanList.groupedPlans.forEach { (type, planes) ->
                if (planes.isNotEmpty()) {
                    stickyHeader {
                        SpendingStickyHeader(
                            title = type.title,
                        )
                    }
                }

                items(planes) { spendingPlan ->
                    SpendingPlanItem(
                        spendingPlan = spendingPlan,
                        onIncomeClick = { onSpendingPlanClick(spendingPlan) },
                        modifier = Modifier.animateContentSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun SpendingStickyHeader(
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

@Composable
private fun SpendingPlanItem(
    spendingPlan: SpendingPlan,
    onIncomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (spendingPlan.selected) main else Gray5
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
                visible = spendingPlan.selected
            ) {
                CheckBox(
                    checked = spendingPlan.selected,
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
                    text = spendingPlan.dateString,
                    style = JUNTheme.typography.titleSmallB,
                )
                Text(
                    text = spendingPlan.title,
                    style = JUNTheme.typography.titleSmallR,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = spendingPlan.amountString,
                    style = JUNTheme.typography.titleNormalB,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}