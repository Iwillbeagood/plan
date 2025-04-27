package jun.money.mate.home.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.utils.currency.CurrencyFormatter

val budgetColor = Color(0xFFfcb72b)
val costColor = Color(0xFFfa4f57)
val saveColor = Color(0xFF13aa6a)

@Composable
internal fun HomePieChart(
    budgetTotal: Long,
    costTotal: Long,
    saveTotal: Long,
) {
    var data by remember {
        mutableStateOf(
            listOf(
                Pie(label = "예산", data = budgetTotal.toDouble(), color = budgetColor, selectedColor = Color.Green, selected = false),
                Pie(label = "지출", data = costTotal.toDouble(), color = costColor, selectedColor = Color.Blue, selected = false),
                Pie(label = "저축", data = saveTotal.toDouble(), color = saveColor, selectedColor = Color.Yellow, selected = false),
            )
        )
    }

    if (budgetTotal == 0L && costTotal == 0L && saveTotal == 0L) {
        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PieChart(
            modifier = Modifier.size(120.dp),
            data = data,
            onPieClick = {
                println("${it.label} Clicked")
                val pieIndex = data.indexOf(it)
                data = data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
            },
            selectedScale = 1.2f,
            scaleAnimEnterSpec = spring<Float>(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            colorAnimEnterSpec = tween(300),
            colorAnimExitSpec = tween(300),
            scaleAnimExitSpec = tween(300),
            spaceDegreeAnimExitSpec = tween(300),
            style = Pie.Style.Stroke(width = 20.dp)
        )

        HorizontalSpacer(1f)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LegendItem(
                color = costColor,
                label = "지출",
                amount = costTotal
            )
            LegendItem(
                color = saveColor,
                label = "저축",
                amount = saveTotal
            )
            LegendItem(
                color = budgetColor,
                label = "예산",
                amount = budgetTotal
            )
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    amount: Long
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 색상 동그라미
        Canvas(modifier = Modifier.size(12.dp)) {
            drawCircle(color = color)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 라벨
        Text(
            text = label,
            style = TypoTheme.typography.titleMediumR,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 금액
        Text(
            text = CurrencyFormatter.formatAmountWon(amount),
            style = TypoTheme.typography.titleMediumB,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun HomePieChartPreview() {
    JunTheme {
        HomePieChart(
            budgetTotal = 1000000,
            costTotal = 500000,
            saveTotal = 200000
        )
    }
}