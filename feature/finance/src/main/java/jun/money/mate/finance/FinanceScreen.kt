package jun.money.mate.finance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
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
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray7
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.finance.component.FinanceChart
import jun.money.mate.finance.component.MoneyChallengeItem
import jun.money.mate.finance.component.PlusButton
import jun.money.mate.model.save.Challenge
import jun.money.mate.ui.LeafIcon
import jun.money.mate.ui.SeedIcon
import jun.money.mate.ui.interop.LocalNavigateActionInterop
import jun.money.mate.ui.interop.rememberShowSnackBar
import jun.money.mate.utils.currency.CurrencyFormatter

@Composable
internal fun FinanceRoute(
    viewModel: FinanceViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.background)

    val showSnackBar = rememberShowSnackBar()
    val navigateAction = LocalNavigateActionInterop.current

    val financeState by viewModel.financeState.collectAsStateWithLifecycle()

    FinanceContent(
        financeState = financeState,
        onShowIncome = navigateAction::navigateToIncomeList,
        onShowSavings = navigateAction::navigateToSaveList,
        onShowChallengeAdd = navigateAction::navigateToChallengeAdd,
        onShowChallengeDetail = navigateAction::navigateToChallengeDetail
    )

    LaunchedEffect(Unit) {
        viewModel.financeEffect.collect { effect ->
        }
    }
}

@Composable
private fun FinanceContent(
    financeState: FinanceState,
    onShowIncome: () -> Unit,
    onShowSavings: () -> Unit,
    onShowChallengeAdd: () -> Unit,
    onShowChallengeDetail: (Long) -> Unit,
) {
    FadeAnimatedVisibility(
        visible = financeState is FinanceState.FinanceData
    ) {
        if (financeState is FinanceState.FinanceData) {
            FinanceScreen(
                totalIncome = financeState.incomeList.total,
                totalSavings = financeState.savePlanList.executedTotal,
                challengeList = financeState.challengeList,
                onShowIncome = onShowIncome,
                onShowSavings = onShowSavings,
                onAddClick = onShowChallengeAdd,
                onChallengeClick = onShowChallengeDetail
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FinanceScreen(
    totalIncome: Long,
    totalSavings: Long,
    challengeList: List<Challenge>,
    onShowIncome: () -> Unit,
    onShowSavings: () -> Unit,
    onAddClick: () -> Unit,
    onChallengeClick: (Long) -> Unit,
) {
    Scaffold{
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                item {
                    FinanceInfos(
                        totalIncome = totalIncome,
                        totalSavings = totalSavings,
                        onShowIncome = onShowIncome,
                        onShowSavings = onShowSavings,
                    )
                }
                stickyHeader {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(top = 28.dp, bottom = 6.dp, start = 20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            Text(
                                text = "저축 챌린지",
                                style = TypoTheme.typography.titleNormalM,
                            )
                            HorizontalSpacer(16.dp)
                            Text(
                                text = "목표를 세우고 도전해보세요!",
                                style = TypoTheme.typography.titleSmallM,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
                items(challengeList) { item ->
                    MoneyChallengeItem(
                        challenge = item,
                        onClick = {
                            onChallengeClick(item.id)
                        },
                    )
                }
                item {
                    Column {
                        PlusButton(
                            onClick = onAddClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FinanceInfos(
    totalIncome: Long,
    totalSavings: Long,
    onShowIncome: () -> Unit,
    onShowSavings: () -> Unit,
) {
    Column {
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
                        text = "이번달 수입",
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
                        text = "이번달 저축",
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
        VerticalSpacer(30.dp)
        HorizontalDivider(10.dp)
    }
}

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
            challengeList = listOf(Challenge.sample),
            onShowIncome = {},
            onShowSavings = {},
            onAddClick = {},
            onChallengeClick = {}
        )
    }
}
