package jun.money.mate.budget.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.theme.Gray8
import jun.money.mate.designsystem.theme.Green3
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme

@Composable
internal fun BudgetChart(budget: Long, amountUsed: Long) {
    val savingsRatio = if (budget == 0L && amountUsed > 0L) {
        1f
    } else if (budget > 0L) {
        amountUsed.toFloat() / budget.toFloat()
    } else {
        0f
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(15.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val barWidth = size.width * savingsRatio
                val cornerRadius = 15f

                drawRoundRect(
                    color = Gray8,
                    topLeft = Offset(0f, 0f),
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )

                val actualCornerRadius =
                    if (barWidth < cornerRadius * 2) CornerRadius(barWidth / 2, barWidth / 2) else CornerRadius(cornerRadius, cornerRadius)

                drawRoundRect(
                    color = Green3,
                    topLeft = Offset(0f, 0f),
                    size = Size(barWidth, size.height),
                    cornerRadius = actualCornerRadius
                )
            }
        }
    }
}

@Preview
@Composable
private fun FinanceChartPreview() {
    JunTheme {
        BudgetChart(budget = 1000, amountUsed = 300)
    }
}
