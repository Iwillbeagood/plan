package jun.money.mate.finance.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray8
import jun.money.mate.designsystem.theme.Green3
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import kotlinx.coroutines.launch

@Composable
internal fun FinanceChart(totalIncome: Long, savings: Long) {
    val savingsRatio = if (totalIncome == 0L && savings > 0L) {
        1f
    } else if (totalIncome > 0L) {
        savings.toFloat() / totalIncome.toFloat()
    } else {
        0f
    }

    val animatedProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(savingsRatio) {
        coroutineScope.launch {
            animatedProgress.animateTo(
                targetValue = savingsRatio,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            )
        }
    }

    val savingsMessage = when {
        savingsRatio == 1f -> "믿기지 않아요! 수입의 100%를 저축했어요! 완벽한 절약왕!"
        savingsRatio >= 0.8f -> "수입의 ${(savingsRatio * 100).toInt()}%를 저축했어요! 최고의 금융 습관이에요!"
        savingsRatio >= 0.7f -> "수입의 ${(savingsRatio * 100).toInt()}%를 저축 중! 안정적인 미래가 보이네요!"
        savingsRatio >= 0.5f -> "수입의 ${(savingsRatio * 100).toInt()}%를 저축했어요! 절반 이상 저축하는 것도 너무 대단해요!"
        savingsRatio >= 0.3f -> "수입의 ${(savingsRatio * 100).toInt()}%를 저축 중이에요! 꾸준히 하면 더 큰 목표도 가능해요!"
        savingsRatio > 0f -> "수입의 ${(savingsRatio * 100).toInt()}%를 저축했어요! 조금씩이라도 저축하는 습관이 중요해요!"
        else -> "아직 저축이 없어요! 다음 달부터 조금씩 시작해볼까요?"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize(),
            ) {
                val barWidth = size.width * animatedProgress.value
                val cornerRadius = 15f

                drawRoundRect(
                    color = Gray8,
                    topLeft = Offset(0f, 0f),
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )

                val actualCornerRadius =
                    if (barWidth < cornerRadius * 2) CornerRadius(barWidth / 2, barWidth / 2) else CornerRadius(cornerRadius, cornerRadius)

                drawRoundRect(
                    color = Green3,
                    topLeft = Offset(0f, 0f),
                    size = Size(barWidth, size.height),
                    cornerRadius = actualCornerRadius,
                )
            }

            val textPadding = maxOf((animatedProgress.value * 150).dp, 10.dp)
            Text(
                text = "${(animatedProgress.value * 100).toInt()}%",
                style = TypoTheme.typography.titleMediumM,
                color = Color.White,
                modifier = Modifier.padding(start = textPadding),
            )
        }
        VerticalSpacer(6.dp)
        HorizontalDivider()
        VerticalSpacer(6.dp)
        Text(
            text = savingsMessage,
            style = TypoTheme.typography.labelLargeR,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun FinanceChartPreview() {
    JunTheme {
        FinanceChart(totalIncome = 1000, savings = 300)
    }
}
