package jun.money.mate.home

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
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.component.TopAppbarType
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray9
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red3
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.main
import jun.money.mate.home.HomeState.HomeData.HomeList
import jun.money.mate.navigation.MainBottomNavItem
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import java.time.LocalDate

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
        onShowMenu = {  },
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
    onShowMenu: () -> Unit,
    onShowNotification: () -> Unit,
    onHomeListClick: (MainBottomNavItem) -> Unit,
    onShowAddScreen: (MainBottomNavItem) -> Unit,
) {
    FadeAnimatedVisibility(
        visible = homeState is HomeState.HomeData
    ) {
        if (homeState is HomeState.HomeData) {
            HomeScreen(
                balance = homeState.balanceString,
                incomeTotal = homeState.incomeList.totalString,
                saveTotal = homeState.savePlanList.totalString,
                homeList = emptyList(),
                onShowMenu = onShowMenu,
                onShowNotification = onShowNotification,
                onHomeListClick = onHomeListClick,
                onShowAddScreen = onShowAddScreen,
            )
        }
    }
}

@Composable
private fun HomeScreen(
    balance: String,
    incomeTotal: String,
    saveTotal: String,
    homeList: List<HomeList>,
    onHomeListClick: (MainBottomNavItem) -> Unit,
    onShowAddScreen: (MainBottomNavItem) -> Unit,
    onShowMenu: () -> Unit,
    onShowNotification: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppbar(
                title = "${LocalDate.now().monthValue}월의 " + stringResource(jun.money.mate.res.R.string.app_name),
                navigationType = TopAppbarType.Custom {
//                    Row(
//                        modifier = Modifier.fillMaxHeight(),
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Notifications,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.onSurface,
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .clickable(onClick = onShowNotification)
//                                .padding(horizontal = 5.dp),
//                        )
//                        Icon(
//                            imageVector = Icons.Default.Menu,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.onSurface,
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .clickable(onClick = onShowMenu)
//                                .padding(horizontal = 5.dp),
//                        )
//                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            VerticalSpacer(20.dp)
            HomeTitle(title = "월간 계획")
            HomeContentBox {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Row {
                        Text(
                            text = "수입",
                            style = TypoTheme.typography.titleMediumM,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "+ $incomeTotal",
                            style = TypoTheme.typography.titleMediumB,
                            color = main
                        )
                    }
                    Row {
                        Text(
                            text = "지출",
                            style = TypoTheme.typography.titleMediumM,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row {
                        Text(
                            text = "저축",
                            style = TypoTheme.typography.titleMediumM,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "- $saveTotal",
                            style = TypoTheme.typography.titleMediumB,
                            color = Red3
                        )
                    }
                }
            }
            VerticalSpacer(10.dp)
            HomeContentBox {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row {
                        Text(
                            text = "잔액",
                            style = TypoTheme.typography.titleMediumM,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = balance,
                            style = TypoTheme.typography.titleMediumB,
                        )
                    }
                }
            }
            VerticalSpacer(30.dp)
            HomeTitle(title = "월간 내역")
            HomeContentBox {
                Column {
                    homeList.forEach { data ->
                        HomeItem(
                            title = stringResource(data.type.titleRes),
                            value = data.value,
                            type = data.type,
                            onClick = { onHomeListClick(data.type) },
                            onAdd = { onShowAddScreen(data.type) },
                        )
                        VerticalSpacer(10.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeContentBox(
    content: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceDim,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun HomeItem(
    title: String,
    value: String,
    type: MainBottomNavItem,
    onClick: () -> Unit,
    onAdd: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceDim,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = value,
                    style = TypoTheme.typography.titleNormalB,
                    textAlign = TextAlign.End,
                )
                Text(
                    text = title,
                    style = TypoTheme.typography.titleSmallM,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            RegularButton(
                onClick = onAdd,
                text = "추가",
                textColor = Black,
                color = Gray9,
                style = TypoTheme.typography.labelLargeM,
            )
        }
    }
}

@Composable
private fun HomeTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = TypoTheme.typography.titleNormalM,
        modifier = modifier.padding(horizontal = 20.dp),
    )
    VerticalSpacer(10.dp)
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    JunTheme {
        HomeScreen(
            balance = "300,000원",
            incomeTotal = "2,000,000원",
            saveTotal = "700,000원",
            onShowMenu = {},
            onShowNotification = {},
            homeList = listOf(
            ),
            onHomeListClick = {},
            onShowAddScreen = {},
        )
    }
}