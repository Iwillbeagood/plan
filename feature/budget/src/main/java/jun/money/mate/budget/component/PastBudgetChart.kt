package jun.money.mate.budget.component

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import jun.money.mate.budget.navigation.NAV_NAME
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray1
import jun.money.mate.designsystem.theme.Gray5
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem.theme.main
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.consumption.PastBudget
import jun.money.mate.utils.currency.CurrencyFormatter
import jun.money.mate.utils.formatYearMonth
import java.time.YearMonth

private val gridProperties = GridProperties(
    xAxisProperties = GridProperties.AxisProperties(
        thickness = .2.dp,
        color = SolidColor(Color.Gray.copy(alpha = .5f)),
        style = StrokeStyle.Dashed(intervals = floatArrayOf(15f, 15f), phase = 10f),
    ),
    yAxisProperties = GridProperties.AxisProperties(
        thickness = .2.dp,
        color = SolidColor(Color.Gray.copy(alpha = .5f)),
        style = StrokeStyle.Dashed(intervals = floatArrayOf(15f, 15f), phase = 10f),
    ),
)
private val dividerProperties = DividerProperties(
    xAxisProperties = LineProperties(
        thickness = .2.dp,
        color = SolidColor(Color.Gray.copy(alpha = .5f)),
        style = StrokeStyle.Dashed(intervals = floatArrayOf(15f, 15f), phase = 10f),
    ),
    yAxisProperties = LineProperties(
        thickness = .2.dp,
        color = SolidColor(Color.Gray.copy(alpha = .5f)),
        style = StrokeStyle.Dashed(intervals = floatArrayOf(15f, 15f), phase = 10f),
    ),
)

@Composable
internal fun PastBudgetChart(
    originBudget: Long,
    pastBudgetGroup: Map<Int, List<PastBudget>>,
    modifier: Modifier = Modifier,
) {
    val thisYear = YearMonth.now().year
    val lastYear = thisYear - 1
    val thisYearBudgets = pastBudgetGroup[thisYear] ?: emptyList()
    val lastYearBudgets = pastBudgetGroup[lastYear] ?: emptyList()

    var isThisYear by remember { mutableStateOf(true) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Gray5, RoundedCornerShape(12.dp)),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = White1,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.size(40.dp),
                    ) {
                        FadeAnimatedVisibility(
                            visible = isThisYear && lastYearBudgets.isNotEmpty(),
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        isThisYear = false
                                    }
                                    .padding(10.dp),
                            )
                        }
                    }
                    Text(
                        text = if (isThisYear) thisYear else { lastYear }.toString(),
                        style = TypoTheme.typography.titleMediumM,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    Box(
                        modifier = Modifier.size(40.dp),
                    ) {
                        FadeAnimatedVisibility(
                            visible = !isThisYear && lastYearBudgets.isNotEmpty(),
                            modifier = Modifier.size(40.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        isThisYear = true
                                    }
                                    .padding(10.dp),
                            )
                        }
                    }
                }
                VerticalSpacer(10.dp)
                PastBudgetChartContent(
                    originBudget = originBudget,
                    pastBudgets = if (isThisYear) {
                        thisYearBudgets
                    } else {
                        lastYearBudgets
                    },
                )
            }
        }
    }
}

@Composable
fun PastBudgetChartContent(
    originBudget: Long,
    pastBudgets: List<PastBudget>,
) {
    val data by remember(pastBudgets) {
        mutableStateOf(
            listOf(
                Line(
                    label = "$NAV_NAME",
                    values = pastBudgets.map {
                        it.budgetRate(originBudget)
                    },
                    color = SolidColor(Gray1),
                    drawStyle = DrawStyle.Stroke(width = 3.dp),
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    popupProperties = PopupProperties(enabled = false),
                ),
                Line(
                    label = "사용량",
                    values = pastBudgets.map(PastBudget::usageRate),
                    color = SolidColor(main),
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(
                        width = 3.dp,
                        strokeStyle = StrokeStyle.Dashed(
                            intervals = floatArrayOf(10f, 10f),
                            phase = 15f,
                        ),
                    ),
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(main),
                        strokeWidth = 2.dp,
                        radius = 3.dp,
                        strokeColor = SolidColor(main),
                    ),
                ),
                Line(
                    label = "",
                    values = listOf(100.0),
                    color = SolidColor(Color.Transparent),
                    drawStyle = DrawStyle.Stroke(),
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    popupProperties = PopupProperties(enabled = false),
                ),
            ),
        )
    }

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp),
        data = data,
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        gridProperties = gridProperties,
        dividerProperties = dividerProperties,
        popupProperties = PopupProperties(
            textStyle = TypoTheme.typography.labelLargeR,
            contentBuilder = {
                CurrencyFormatter.formatAmount((it / 90.0) * (originBudget))
            },
            containerColor = MaterialTheme.colorScheme.background.copy(
                alpha = 0.2f,
            ),
        ),
        indicatorProperties = HorizontalIndicatorProperties(enabled = false),
        labelProperties = LabelProperties(
            enabled = true,
            labels = pastBudgets.map { formatYearMonth(it.date) },
            textStyle = TypoTheme.typography.labelLargeM,
        ),
        labelHelperProperties = LabelHelperProperties(textStyle = TypoTheme.typography.labelLargeR),
        curvedEdges = true,
    )
}

@Preview
@Composable
private fun Preview() {
    JunTheme {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            PastBudgetChart(
                originBudget = 1000000,
                pastBudgetGroup = Budget.sample.groupedPastBudget,
            )
        }
    }
}
