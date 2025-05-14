package jun.money.mate.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.StateAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.Blue1
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray9
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.home.component.HomePieChart
import jun.money.mate.navigation.MainBottomNavItem
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.res.R
import jun.money.mate.utils.currency.CurrencyFormatter

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    ChangeStatusBarColor()

    val showSnackBar = rememberShowSnackBar()
    val navigateAction = LocalNavigateActionInterop.current

    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    HomeContent(
        homeState = homeState,
        onShowNotification = { },
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
            Row(
                modifier = Modifier.padding(start = 20.dp, top = 4.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.width(60.dp),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it),
        ) {
            BalanceText(
                balance = balance,
                incomeTotal = incomeTotal,
            )
            VerticalSpacer(16.dp)
            Surface(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Gray9),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                HomePieChart(
                    budgetTotal = budgetTotal,
                    costTotal = costTotal,
                    saveTotal = saveTotal,
                )
            }
            VerticalSpacer(30.dp)
        }
    }
}

@Composable
internal fun BalanceText(
    balance: Long,
    incomeTotal: Long,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Gray9),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "잔액",
                style = TypoTheme.typography.titleSmallM,
            )
            HorizontalSpacer(10.dp)
            Text(
                text = CurrencyFormatter.formatAmount(balance),
                style = TypoTheme.typography.headlineLargeB,
                color = if (balance >= -+0) Black else Red3,
            )
            VerticalSpacer(4.dp)
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "이번달의 총 수익은 ",
                    style = TypoTheme.typography.labelLargeM,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                )
                Text(
                    text = CurrencyFormatter.formatAmountWon(incomeTotal),
                    style = TypoTheme.typography.labelLargeB,
                    color = Blue1,
                    textAlign = TextAlign.End,
                )
                Text(
                    text = " 이였어요",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = TypoTheme.typography.labelLargeM,
                    textAlign = TextAlign.End,
                )
            }
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
