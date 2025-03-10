package jun.money.mate.finance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray7
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem_date.datetimepicker.MonthBar
import jun.money.mate.finance.component.FinanceChart
import jun.money.mate.ui.LeafIcon
import jun.money.mate.ui.SeedIcon
import jun.money.mate.ui.interop.LocalNavigateActionInterop
import jun.money.mate.ui.interop.rememberShowSnackBar
import jun.money.mate.utils.currency.CurrencyFormatter
import java.time.YearMonth

@Composable
internal fun FinanceRoute(
    viewModel: FinanceViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.background)
    val showSnackBar = rememberShowSnackBar()
    val navigateAction = LocalNavigateActionInterop.current

    val financeState by viewModel.financeState.collectAsStateWithLifecycle()
    val month by viewModel.month.collectAsStateWithLifecycle()

    FinanceContent(
        financeState = financeState,
        viewModel = viewModel,
        month = month,
        onShowIncome = { navigateAction.navigateToIncomeList(month) },
        onShowSavings = { navigateAction.navigateToSaveList(month) }
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
    month: YearMonth,
    onShowIncome: () -> Unit,
    onShowSavings: () -> Unit,
) {
    FadeAnimatedVisibility(
        visible = financeState is FinanceState.FinanceData
    ) {
        if (financeState is FinanceState.FinanceData) {
            FinanceScreen(
                totalIncome = financeState.incomeList.total,
                totalSavings = financeState.savePlanList.executedTotal,
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
    month: YearMonth,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onShowIncome: () -> Unit,
    onShowSavings: () -> Unit,
) {
    Scaffold(
        topBar = {
            Column {
                VerticalSpacer(20.dp)
                MonthBar(
                    month = month,
                    onPrev = onPrev,
                    onNext = onNext,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                FinanceInfos(
                    totalIncome = totalIncome,
                    totalSavings = totalSavings,
                    onShowIncome = onShowIncome,
                    onShowSavings = onShowSavings
                )
            }
        }
    }
}

@Composable
private fun FinanceInfos(
    totalIncome: Long,
    totalSavings: Long,
    onShowIncome: () -> Unit,
    onShowSavings: () -> Unit
) {
    VerticalSpacer(20.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(16.dp)
    ) {
        FinanceBox(
            onClick = onShowIncome,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
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
                HorizontalSpacer(1f)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            VerticalSpacer(1f)
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
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
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
                HorizontalSpacer(1f)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
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



/**
 * 만약 저축 목표를 넣는다면, 매달 얼마만큼 납부를 했는지 체크하는 것이 좋을 것 같음. 이는 저축 리스트에서도 보여야 할 것 같음.
 * 다만 좀 다르게 보여야 할거 같음. 가장 상단에 저축 목표로 보이는 방식으로. 그래서 데이터 베이스를 완전히 새롭게 파는것도 좋아보임.
 * 저축 계획은 이 화면에서 리스트 형식으로 보이는 것이 좋아보임. 이 화면에서 바로 추가하기 버튼이 존재해서 화면으로 넘어가게 할듯.
 * 상세 화면에서는 전체 계획이 보이고, 해당 달에 납부를 하지않았으면 자동으로 계획이 연장됨.
 * */
@Composable
private fun FinanceBox(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceDim,
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color,
        border = BorderStroke(1.dp, Gray7),
        onClick = onClick,
        enabled = onClick != {},
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
            totalSavings = 300000,
            month = YearMonth.now(),
            onPrev = {},
            onNext = {},
            onShowIncome = {},
            onShowSavings = {}
        )
    }
}
