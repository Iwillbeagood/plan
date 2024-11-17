package jun.money.mate.spending_plan.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray5
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.model.spending.ConsumptionSpend
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingType
import java.time.LocalDate

@Composable
internal fun ConsumptionPlanItem(
    consumptionSpend: ConsumptionSpend,
    onIncomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spendingPlan = consumptionSpend.spendingPlan

    SpendingItemContainer(
        selected = spendingPlan.selected,
        onClick = onIncomeClick,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .animateContentSize()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = spendingPlan.title,
                    style = JUNTheme.typography.titleLargeM,
                )
                VerticalSpacer(10.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "예산",
                        style = JUNTheme.typography.titleSmallR,
                    )
                    Text(
                        text = spendingPlan.amountString,
                        style = JUNTheme.typography.titleMediumM,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
                // 사용 금액은 전체 지출내역을 파악해서 지출 내역을 바탕으로 사용금액을 나타내야 함
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "이번달 사용금액",
                        style = JUNTheme.typography.titleSmallR,
                    )
                    Text(
                        text = consumptionSpend.totalString,
                        style = JUNTheme.typography.titleMediumM,
                        color = Red3,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
                VerticalSpacer(10.dp)
                HorizontalDivider()
                VerticalSpacer(10.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "남은 금액",
                        style = JUNTheme.typography.titleSmallM,
                    )
                    Text(
                        text = consumptionSpend.remainingString,
                        style = JUNTheme.typography.titleMediumB,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ConsumptionPlanItemPreview() {
    JunTheme {
        ConsumptionPlanItem(
            consumptionSpend = ConsumptionSpend(
                SpendingPlan(
                    id = 1,
                    title = "식비",
                    amount = 10000,
                    spendingCategoryName = "식비",
                    planDay = 12,
                    type = SpendingType.ConsumptionPlan,
                    isApply = false,
                ),
                consumptionTotal = 1000
            ),
            onIncomeClick = {},
        )
    }
}