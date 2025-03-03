package jun.money.mate.finance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem_date.datetimepicker.MonthBar
import jun.money.mate.finance.component.FinanceChart
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.ui.LeafIcon
import jun.money.mate.ui.SeedIcon
import jun.money.mate.utils.currency.CurrencyFormatter
import java.time.LocalDate

@Composable
internal fun FinanceRoute(
    onShowIncome: () -> Unit,
    onShowSavings: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: FinanceViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

    val financeState by viewModel.financeState.collectAsStateWithLifecycle()
    val month by viewModel.month.collectAsStateWithLifecycle()

    FinanceContent(
        financeState = financeState,
        viewModel = viewModel,
        month = month,
        onShowIncome = onShowIncome,
        onShowSavings = onShowSavings
    )

    LaunchedEffect(Unit) {
        viewModel.financeEffect.collect { effect ->
        }
    }
}

@Composable
private fun FinanceContent(
    financeState: FinanceState,
    viewModel: FinanceViewModel,
    month: LocalDate,
    onShowIncome: () -> Unit,
    onShowSavings: () -> Unit,
) {
    FadeAnimatedVisibility(
        visible = financeState is FinanceState.FinanceData
    ) {
        if (financeState is FinanceState.FinanceData) {
            FinanceScreen(
                totalIncome = financeState.incomeList.total,
                totalSavings = financeState.savePlanList.total,
                month = month,
                onPrev = viewModel::prevMonth,
                onNext = viewModel::nextMonth,
                onShowIncome = onShowIncome,
                onShowSavings = onShowSavings
            )
        }
    }
}

@Composable
private fun FinanceScreen(
    totalIncome: Long,
    totalSavings: Long,
    month: LocalDate,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onShowIncome: () -> Unit,
    onShowSavings: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            VerticalSpacer(20.dp)
            MonthBar(
                month = month,
                onPrev = onPrev,
                onNext = onNext,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            VerticalSpacer(20.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                FinanceBox(
                    onClick = onShowIncome,
                    modifier = Modifier.weight(1f)
                ) {
                    VerticalSpacer(10.dp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "수입",
                            style = TypoTheme.typography.titleMediumM,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        HorizontalSpacer(4.dp)
                        LeafIcon(
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    VerticalSpacer(30.dp)
                    Text(
                        text = CurrencyFormatter.formatToWon(totalIncome),
                        style = TypoTheme.typography.titleLargeB,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                    )
                    VerticalSpacer(10.dp)
                }
                HorizontalSpacer(16.dp)
                FinanceBox(
                    onClick = onShowSavings,
                    modifier = Modifier.weight(1f)
                ) {
                    VerticalSpacer(10.dp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "저축",
                            style = TypoTheme.typography.titleMediumM,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        HorizontalSpacer(2.dp)
                        SeedIcon(
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    VerticalSpacer(30.dp)
                    Text(
                        text = CurrencyFormatter.formatToWon(totalSavings),
                        style = TypoTheme.typography.titleLargeB,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                    )
                    VerticalSpacer(10.dp)
                }
            }
            FinanceBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                VerticalSpacer(10.dp)
                Text(
                    text = "저축 비율",
                    style = TypoTheme.typography.titleMediumM,
                    modifier = Modifier.padding(start = 16.dp)
                )
                FinanceChart(
                    totalIncome = totalIncome,
                    savings = totalSavings
                )
            }
        }
    }
}

@Composable
private fun FinanceBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceDim,
        onClick = onClick,
        enabled = onClick != {},
        contentColor = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 2.dp,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            content()
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun FinanceScreenPreview() {
    JunTheme {
        FinanceScreen(
            totalIncome = 1000000,
            totalSavings = 500000,
            month = LocalDate.now(),
            onPrev = {},
            onNext = {},
            onShowIncome = {},
            onShowSavings = {}
        )
    }
}
