package jun.money.mate.challenge

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.challenge.contract.SavingListEffect
import jun.money.mate.challenge.contract.SavingListState
import jun.money.mate.designsystem.component.DefaultSwitch
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TopAppbarIcon
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray4
import jun.money.mate.designsystem.theme.Gray7
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.main10
import jun.money.mate.model.save.ChallengeDateType
import jun.money.mate.model.save.ChallengeProgress
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.res.R
import jun.money.mate.ui.interop.LocalNavigateActionInterop
import jun.money.mate.ui.interop.rememberPopBackStack
import jun.money.mate.ui.interop.rememberShowSnackBar
import jun.money.mate.utils.formatDateBasedOnYear
import java.time.YearMonth

@Composable
internal fun SaveListRoute(
    viewModel: SavingListViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(main10)

    val navigateAction = LocalNavigateActionInterop.current
    val showSnackBar = rememberShowSnackBar()
    val popBackStack = rememberPopBackStack()
    val savingListState by viewModel.savingListState.collectAsStateWithLifecycle()

    SavingListScreen(
        savingListState = savingListState,
        month = viewModel.month,
        onShowDetail = navigateAction::navigateToSavingDetail,
        onSavingAdd = navigateAction::navigateToSavingAdd,
        onExecuteChange = viewModel::executeChange,
        onGoBack = popBackStack,
    )

    LaunchedEffect(Unit) {
        viewModel.savingListEffect.collect { effect ->
            when (effect) {
                is SavingListEffect.EditSpendingPlan -> navigateAction.navigateToSavingDetail(effect.id)
                is SavingListEffect.ShowSnackBar -> showSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun SavingListScreen(
    savingListState: SavingListState,
    month: YearMonth,
    onShowDetail: (Long) -> Unit,
    onSavingAdd: () -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
    onGoBack: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            RegularButton(
                text = "추가",
                onClick = onSavingAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceDim)
                    .padding(16.dp)
            )
        },
        containerColor = main10
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                SavingListContent(
                    savingListState = savingListState,
                    onShowDetail = onShowDetail,
                    onExecuteChange = onExecuteChange,
                    modifier = Modifier.weight(1f)
                )
            }
            FadeAnimatedVisibility(
                visible = savingListState is SavingListState.SavingListData,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                if (savingListState is SavingListState.SavingListData) {
                    Column(
                        modifier = Modifier.padding(start = 30.dp, top = 60.dp)
                    ) {
                        Text(
                            text = "${formatDateBasedOnYear(month)} 총 납입금액",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = TypoTheme.typography.headlineSmallM,
                        )
                        Text(
                            text = savingListState.savePlanList.totalString,
                            style = TypoTheme.typography.displaySmallB,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable(onClick = onGoBack)
                    .align(Alignment.TopStart),
            ) {
                TopAppbarIcon(
                    iconId = jun.money.mate.designsystem.R.drawable.ic_back,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun SavingListContent(
    savingListState: SavingListState,
    onShowDetail: (Long) -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    FadeAnimatedVisibility(
        savingListState is SavingListState.SavingListData,
        modifier = modifier
    ) {
        if (savingListState is SavingListState.SavingListData) {
        }
    }
}

@Composable
private fun MoneyChallengeItem(
    challengeProgress: ChallengeProgress,
    onAchieveChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val param = when (challengeProgress.dateType) {
        ChallengeDateType.Today -> MoneyChallengeItemParam(
            icon = if (challengeProgress.isAchieved) jun.money.mate.res.R.drawable.ic_smile_flower else jun.money.mate.res.R.drawable.ic_upset_flower,
            textColor = Black,
            iconSize = 48
        )
        ChallengeDateType.PAST -> MoneyChallengeItemParam(
            icon = if (challengeProgress.isAchieved) jun.money.mate.res.R.drawable.ic_smile_flower else jun.money.mate.res.R.drawable.ic_upset_flower,
        )

        ChallengeDateType.UPCOMING -> MoneyChallengeItemParam(
            icon = jun.money.mate.res.R.drawable.ic_smile_flower,
        )
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceDim,
        border = BorderStroke(1.dp, Gray7),
        shadowElevation = 2.dp,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Crossfade(targetState = challengeProgress.isAchieved, label = "flower_animation") { achieved ->
                Image(
                    painter = painterResource(
                        id = if (achieved) jun.money.mate.res.R.drawable.ic_smile_flower else jun.money.mate.res.R.drawable.ic_upset_flower
                    ),
                    contentDescription = "Flower Image",
                    colorFilter = ColorFilter.tint(
                        color = if (achieved) Color.Unspecified else Gray4
                    ),
                    modifier = Modifier.size(param.iconSize.dp)
                )
            }
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${challengeProgress.index}회차",
                    style = if (challengeProgress.isAchieved)
                        TypoTheme.typography.titleMediumM
                    else
                        TypoTheme.typography.titleLargeB,
                    color = param.textColor
                )
                Text(
                    text = formatDateBasedOnYear(challengeProgress.date),
                    style = TypoTheme.typography.titleSmallM,
                    color = param.textColor
                )
            }
            Column {
                DefaultSwitch(
                    checked = challengeProgress.isAchieved,
                    onCheckedChange = onAchieveChange
                )
            }
        }
    }
}

@Composable
private fun FlowerImage(
    size: Int,
    isAchieved: Boolean,
) {
    Crossfade(targetState = isAchieved, label = "flower_animation") { achieved ->
        Image(
            painter = painterResource(
                id = if (achieved) jun.money.mate.res.R.drawable.ic_smile_flower else R.drawable.ic_upset_flower
            ),
            contentDescription = "Flower Image",
            colorFilter = ColorFilter.tint(
                color = if (achieved) Color.Unspecified else Gray4
            ),
            modifier = Modifier.size(size.dp)
        )
    }
}

/**
 * 일단 이미 지나간 것들에 대해서는 날짜와 스위치가 보여야 함.
 * 현재 체크해야하는 것도 날짜와 스위치가 보여야하고, 강조 표시가 되어야 할듯
 * 아직 오지 않는 것들에 대해서는 날짜만 보여야 할듯
 * */

data class MoneyChallengeItemParam(
    val icon: Int,
    val iconSize: Int = 24,
    val textColor: Color = Gray4,
)


@Preview(showBackground = true)
@Composable
private fun SpendingListScreenPreview() {
    JunTheme {
        SavingListScreen(
            savingListState = SavingListState.SavingListData(SavePlanList.sample),
            month = YearMonth.now(),
            onSavingAdd = {},
            onShowDetail = {},
            onGoBack = {},
            onExecuteChange = { _, _ -> },
        )
    }
}