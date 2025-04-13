package jun.money.mate.budget.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray8
import jun.money.mate.designsystem.theme.Green3
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.utils.currency.CurrencyFormatter

@Composable
internal fun BudgetChart(budget: Long, amountUsed: Long) {
    val usedRatio = if (budget == 0L && amountUsed > 0L) {
        1f
    } else if (budget > 0L) {
        amountUsed.toFloat() / budget.toFloat()
    } else {
        0f
    }

    val barLength = remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = max(barLength.value - 40.dp, 0.dp))
        ) {
            Text(
                text = CurrencyFormatter.formatAmountWon(amountUsed),
                style = TypoTheme.typography.titleMediumM,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = "",
            )
        }
        VerticalSpacer(4.dp)
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
                    val barWidth = size.width * usedRatio
                    val cornerRadius = 15f
                    val barWidthInDp = barWidth.toDp()
                    barLength.value = barWidthInDp

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
}

@Preview(showBackground = true)
@Composable
private fun BudgetChartPreview() {
    JunTheme {
        BudgetChart(budget = 1000, amountUsed = 300)
    }
}
