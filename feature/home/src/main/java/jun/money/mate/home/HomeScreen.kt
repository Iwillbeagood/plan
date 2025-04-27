package jun.money.mate.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.StateAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.Blue1
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray7
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.home.component.HomePieChart
import jun.money.mate.navigation.MainBottomNavItem
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.res.R
import jun.money.mate.utils.currency.CurrencyFormatter

/**
 * 홈 화면 어떻게 구성할 지
 * 일단 가장 상단에 쿼카 아이콘 넣자
 * 내 엡에 있는 모든 기능
 * 고정 지출, 저축, 예산 3개르 추가적으로 보여줘야함.
 *
 *
 *
 * 수입을 바탕으로 수입에서의 나머지 항목들을 오벼주고 챌린지는 별도로 보여주면 될듯.
 * 이거를 원 그래프로 보여주는 것은 별로인거 같고,
 *
 * 좀 한눈에 수익과 지출을 보여주고 싶은데,, 수익에서 얼마나 지출하고 있는지 또 오바하고 있으면 그것을 보여주면 좋겠어. 아이디어를..
 *
 * 수익의 얼마만큼을 저축하고있고, 얼마를 소비하고 있는지 보여주면 좋겠음.
 * */
@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

    val showSnackBar = rememberShowSnackBar()
    val navigateAction = LocalNavigateActionInterop.current

    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    HomeContent(
        homeState = homeState,
        onShowNotification = {  },
        onHomeListClick = viewModel::navigateTo,
        onShowAddScreen = viewModel::navigateToAdd,
    )

    LaunchedEffect(Unit) {
        viewModel.homeEffect.collect { effect ->
            when (effect) {
                HomeEffect.ShowIncomeAddScreen -> navigateAction.navigateToIncomeAdd()
                HomeEffect.ShowSpendingAddScreen -> {

                }
                HomeEffect.ShowConsumptionAddScreen -> navigateAction.navigateToBudgetAdd()
                HomeEffect.ShowSaveAddScreen -> navigateAction.navigateToSavingAdd()
                is HomeEffect.ShowMainNavScreen -> navigateAction.navigateBottomNav(effect.navItem)
            }
        }
    }
}

@Composable
private fun HomeContent(
    homeState: HomeState,
    onShowNotification: () -> Unit,
    onHomeListClick: (MainBottomNavItem) -> Unit,
    onShowAddScreen: (MainBottomNavItem) -> Unit,
) {
    StateAnimatedVisibility<HomeState.HomeData>(
        target = homeState,
    ) {
        HomeScreen(
            balance = it.balance,
            incomeTotal = it.incomeList.total,
            saveTotal = it.savePlanList.total,
            budgetTotal = it.budgetTotal,
            costTotal = it.costTotal,
        )
    }
}

@Composable
private fun HomeScreen(
    balance: Long,
    incomeTotal: Long,
    saveTotal: Long,
    budgetTotal: Long,
    costTotal: Long,
) {
    Scaffold(
        topBar = {
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            BalanceText(
                balance = balance,
                incomeTotal = incomeTotal,
            )
            VerticalSpacer(40.dp)
            HorizontalDivider(10.dp)
            VerticalSpacer(30.dp)
            Column(
                 modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Gray7),
                    shadowElevation = 1.dp,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "자금 구성",
                            style = TypoTheme.typography.titleNormalM,
                            modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                        )
                        HomePieChart(
                            budgetTotal = budgetTotal,
                            costTotal = costTotal,
                            saveTotal = saveTotal
                        )
                    }
                }
                VerticalSpacer(30.dp)
            }
        }
    }
}

@Composable
internal fun BalanceText(
    balance: Long,
    incomeTotal: Long,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "잔액",
                style = TypoTheme.typography.headlineSmallM,
            )
            HorizontalSpacer(10.dp)
            Text(
                text = CurrencyFormatter.formatAmountWon(balance),
                style = TypoTheme.typography.headlineLargeB,
                color = if (balance > 0) Black else Red3,
            )
        }
        VerticalSpacer(4.dp)
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "이번달의 총 수익은 ",
                style = TypoTheme.typography.titleSmallM,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
            )
            Text(
                text = CurrencyFormatter.formatAmountWon(incomeTotal),
                style = TypoTheme.typography.titleSmallB,
                color = Blue1,
                textAlign = TextAlign.End,
            )
            Text(
                text = " 이였어요",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TypoTheme.typography.titleSmallM,
                textAlign = TextAlign.End,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    JunTheme {
        HomeScreen(
            balance = 300000,
            incomeTotal = 1000000,
            saveTotal = 10000,
            budgetTotal = 100000,
            costTotal = 1000000,
        )
    }
}