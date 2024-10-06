package jun.money.mate.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.ehsannarmani.compose_charts.models.Pie
import jun.money.mate.designsystem.component.HmTopAppbarType
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
import jun.money.mate.res.R
import jun.money.mate.ui.SplashScreen
import jun.money.mate.utils.currency.CurrencyFormatter
import java.time.LocalDate

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    HomeContent(
        homeState,
        viewModel,
    )
}

@Composable
private fun HomeContent(
    homeState: HomeState,
    viewModel: HomeViewModel,
) {
    when (homeState) {
        HomeState.Loading -> SplashScreen()
        is HomeState.HomeData -> {
            HomeScreen(
                isShowPieChart = homeState.isShowPieChart,
                pieList = homeState.pieList,
                onShowMenu = { },
                onShowNotification = { },
            )
        }
    }
}

@Composable
private fun HomeScreen(
    isShowPieChart: Boolean,
    pieList: List<Pie>,
    onShowMenu: () -> Unit,
    onShowNotification: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppbar(
                title = stringResource(R.string.app_name),
                backgroundColor = MaterialTheme.colorScheme.surface,
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
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            VerticalSpacer(20.dp)
            HomeTitle("${LocalDate.now().monthValue}월 전체 내역")
            HomeField(
                title = "전체 수입",
                value = "+100원",
                valueColor = main
            )
            VerticalSpacer(10.dp)
            HomeField(
                title = "전체 지출",
                value = "-100원",
                valueColor = Red3
            )
            VerticalSpacer(30.dp)
            HomeTitle("지출 차트")
            if (isShowPieChart) {
                HomePieChart(pieList)
            }
            pieList.forEach {
                HomeBox(
                    color = it.color
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = it.label!!,
                            style = JUNTheme.typography.titleMediumM,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 5.dp)
                        )
                        Text(
                            text = "-" + CurrencyFormatter.formatAmountWon(it.data),
                            style = JUNTheme.typography.titleMediumB,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
                VerticalSpacer(10.dp)
            }
        }
    }
}

@Composable
private fun HomeField(
    title: String,
    value: String,
    valueColor: Color,
    isEmpty: Boolean = true
) {
    HomeBox {
        Row {
            Text(
                text = title,
                style = JUNTheme.typography.titleMediumM,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = if (isEmpty) "+ 추가하기" else "자세히보기 >",
                style = JUNTheme.typography.labelLargeR,
                color = Gray1
            )
        }
        Text(
            text = if (isEmpty) "내역이 존재하지 않습니다" else value,
            style = JUNTheme.typography.titleNormalB,
            color = valueColor,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun HomeBox(
    color: Color = MaterialTheme.colorScheme.surfaceDim,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray6),
        color = color,
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
            isShowPieChart = true,
            pieList = listOf(
                Pie(label = "정기 지출", data = 100.0, color = Yellow1, selected = true),
                Pie(label = "변동 지출", data = 200.0, color = Green2, selected = false),
            ),
            onShowMenu = {},
            onShowNotification = {},
        )
    }
}