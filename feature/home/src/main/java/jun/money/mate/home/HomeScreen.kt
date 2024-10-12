package jun.money.mate.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.ehsannarmani.compose_charts.models.Pie
import jun.money.mate.designsystem.component.HmTopAppbarType
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray1
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.Green2
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem.theme.Yellow1
import jun.money.mate.designsystem.theme.main
import jun.money.mate.home.component.HomePieChart
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.ui.SplashScreen
import jun.money.mate.utils.currency.CurrencyFormatter
import java.time.LocalDate

@Composable
internal fun HomeRoute(
    onShowMenu: () -> Unit,
    onShowNotification: () -> Unit,
    onShowIncomeList: () -> Unit,
    onShowIncomeAdd: () -> Unit,
    onShowSpendingList: () -> Unit,
    onShowSpendingAdd: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    HomeContent(
        homeState = homeState,
        viewModel = viewModel,
        onShowMenu = onShowMenu,
        onShowNotification = onShowNotification,
    )

    LaunchedEffect(Unit) {
        viewModel.homeEffect.collect { effect ->
            when (effect) {
                HomeEffect.ShowIncomeAddScreen -> onShowIncomeAdd()
                HomeEffect.ShowIncomeListScreen -> onShowIncomeList()
                HomeEffect.ShowSpendingAddScreen -> onShowSpendingList()
                HomeEffect.ShowSpendingListScreen -> onShowSpendingAdd()
                HomeEffect.ShowSpendingPlanListScreen -> {}
            }
        }
    }
}

@Composable
private fun HomeContent(
    homeState: HomeState,
    viewModel: HomeViewModel,
    onShowMenu: () -> Unit,
    onShowNotification: () -> Unit,
) {
    when (homeState) {
        HomeState.Loading -> SplashScreen()
        is HomeState.HomeData -> {
            HomeScreen(
                balance = homeState.balanceString,
                incomeTotal = homeState.incomeList.totalString,
                spendingTotal = homeState.spendingPlanList.totalString,
                isShowPieChart = homeState.isShowPieChart,
                pieList = homeState.pieList,
                onIncomeTotalClick = viewModel::showIncomeScreen,
                onSpendingTotalClick = viewModel::showSpendingScreen,
                onShowMenu = onShowMenu,
                onShowNotification = onShowNotification,
            )
        }
    }
}

@Composable
private fun HomeScreen(
    balance: String,
    incomeTotal: String,
    spendingTotal: String,
    isShowPieChart: Boolean,
    pieList: List<Pie>,
    onIncomeTotalClick: () -> Unit,
    onSpendingTotalClick: () -> Unit,
    onShowMenu: () -> Unit,
    onShowNotification: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppbar(
                title = "${LocalDate.now().monthValue}월의 머니 메이트",
                navigationType = HmTopAppbarType.Custom {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable(onClick = onShowNotification)
                                .padding(horizontal = 5.dp),
                        )
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable(onClick = onShowMenu)
                                .padding(horizontal = 5.dp),
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier.padding(30.dp)
            ) {
                Text(
                    text = "이번달 남은 예산",
                    style = JUNTheme.typography.titleLargeM,
                )
                VerticalSpacer(10.dp)
                Text(
                    text = balance,
                    style = JUNTheme.typography.headlineSmallB,
                )
            }
            HorizontalDivider(thickness = 10.dp)
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                VerticalSpacer(20.dp)
                HomeTitle("${LocalDate.now().monthValue}월 내역")
                VerticalSpacer(5.dp)
                HomeField(
                    title = "전체 수입",
                    value = incomeTotal,
                    onClick = onIncomeTotalClick,
                    valueColor = main
                )
                VerticalSpacer(10.dp)
                HomeField(
                    title = "전체 지출",
                    value = spendingTotal,
                    onClick = onSpendingTotalClick,
                    valueColor = Red3
                )
                VerticalSpacer(30.dp)
                if (isShowPieChart) {
                    HomeTitle("지출 차트")
                    HomePieChart(pieList)
                    HomeBorderBox {
                        pieList.forEach { pie ->
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Circle,
                                    tint = pie.color,
                                    contentDescription = null,
                                )
                                Text(
                                    text = pie.label!!,
                                    style = JUNTheme.typography.titleMediumM,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 10.dp)
                                )
                                Text(
                                    text = "-" + CurrencyFormatter.formatAmountWon(pie.data),
                                    style = JUNTheme.typography.titleMediumB,
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun HomeField(
    onClick: () -> Unit,
    title: String,
    value: String,
    valueColor: Color
) {
    HomeBox(
        onClick = onClick
    ) {
        Row {
            Text(
                text = title,
                style = JUNTheme.typography.titleMediumM,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "자세히보기 >",
                style = JUNTheme.typography.labelLargeR,
                color = Gray1
            )
        }
        Text(
            text = value,
            style = JUNTheme.typography.titleNormalB,
            color = valueColor,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun HomeBox(
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.surfaceDim,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray6),
        color = color,
        onClick = onClick,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun HomeBorderBox(
    color: Color = Gray6,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, color),
        color = MaterialTheme.colorScheme.surfaceDim,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun HomeTitle(title: String) {
    Text(
        text = title,
        style = JUNTheme.typography.titleNormalB,
    )
    VerticalSpacer(10.dp)
}

@Preview
@Composable
private fun HomeScreenPreview() {
    JunTheme {
        HomeScreen(
            balance = "-100,000원",
            incomeTotal = "100,000원",
            spendingTotal = "-200,000원",
            isShowPieChart = true,
            pieList = listOf(
                Pie(label = "정기 지출", data = 100.0, color = Yellow1, selected = true),
                Pie(label = "변동 지출", data = 200.0, color = Green2, selected = false),
            ),
            onShowMenu = {},
            onShowNotification = {},
            onIncomeTotalClick = {},
            onSpendingTotalClick = {},
        )
    }
}