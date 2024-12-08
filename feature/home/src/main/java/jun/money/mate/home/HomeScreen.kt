package jun.money.mate.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.CircleIcon
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.component.TopAppbarType
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray9
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.home.HomeState.HomeData.HomeList
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainBottomNavItem
import java.time.LocalDate

@Composable
internal fun HomeRoute(
    onShowMenu: () -> Unit,
    onShowNotification: () -> Unit,
    onShowMainNavScreen: (MainBottomNavItem) -> Unit,
    onShowIncomeAdd: () -> Unit,
    onShowSpendingAdd: () -> Unit,
    onShowSaveAdd: () -> Unit,
    onShowConsumptionAdd: () -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    HomeContent(
        homeState = homeState,
        onShowMenu = onShowMenu,
        onShowNotification = onShowNotification,
        onHomeListClick = viewModel::navigateTo,
        onShowAddScreen = viewModel::navigateTo,
    )

    LaunchedEffect(Unit) {
        viewModel.homeEffect.collect { effect ->
            when (effect) {
                HomeEffect.ShowIncomeAddScreen -> onShowIncomeAdd()
                HomeEffect.ShowSpendingAddScreen -> onShowSpendingAdd()
                HomeEffect.ShowConsumptionAddScreen -> onShowConsumptionAdd()
                HomeEffect.ShowSaveAddScreen -> onShowSaveAdd()
                is HomeEffect.ShowMainNavScreen -> onShowMainNavScreen(effect.navItem)
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
                homeList = homeState.homeList,
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
    homeList: List<HomeList>,
    onHomeListClick: (MainBottomNavItem) -> Unit,
    onShowAddScreen: (MainBottomNavItem) -> Unit,
    onShowMenu: () -> Unit,
    onShowNotification: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppbar(
                title = "${LocalDate.now().monthValue}월의 머니 메이트",
                navigationType = TopAppbarType.Custom {
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
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            VerticalSpacer(20.dp)
            HomeContentBox {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = "남은 금액",
                        style = JUNTheme.typography.titleLargeM,
                    )
                    VerticalSpacer(10.dp)
                    Text(
                        text = balance,
                        style = JUNTheme.typography.headlineSmallB,
                    )
                }
            }
            VerticalSpacer(30.dp)
            HomeTitle(
                title = "월간 내역",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .padding(10.dp)
            .clickable(onClick = onClick),
    ) {
        CircleIcon(
            icon = type.icon,
            tint = type.color,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = value,
                style = JUNTheme.typography.titleNormalM,
                textAlign = TextAlign.End,
            )
            Text(
                text = title,
                style = JUNTheme.typography.titleSmallM,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        RegularButton(
            onClick = onAdd,
            text = "추가",
            textColor = Black,
            color = Gray9,
            style = JUNTheme.typography.labelLargeM,
        )
    }
}

@Composable
private fun HomeTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = JUNTheme.typography.titleNormalM,
        modifier = modifier,
    )
    VerticalSpacer(10.dp)
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    JunTheme {
        HomeScreen(
            balance = "100,000원",
            onShowMenu = {},
            onShowNotification = {},
            homeList = listOf(
                HomeList(
                    value = "100,000원",
                    type = MainBottomNavItem.Income
                ),
                HomeList(
                    value = "100,000원",
                    type = MainBottomNavItem.SpendingPlan
                ),
                HomeList(
                    value = "100,000원",
                    type = MainBottomNavItem.Save
                ),
                HomeList(
                    value = "100,000원",
                    type = MainBottomNavItem.ConsumptionSpend
                ),
            ),
            onHomeListClick = {},
            onShowAddScreen = {},
        )
    }
}