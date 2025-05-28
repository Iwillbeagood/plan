package jun.money.mate.save

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.StateAnimatedVisibility
import jun.money.mate.designsystem.component.TopAppbarIcon
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.main10
import jun.money.mate.designsystem.theme.main2
import jun.money.mate.designsystem.theme.main210
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.model.save.SavingChallenge
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.save.component.AcornBox
import jun.money.mate.save.component.SaveListBody
import jun.money.mate.save.contract.SavingListEffect
import jun.money.mate.save.contract.SavingListState
import java.time.YearMonth

@Composable
internal fun SaveListRoute(
    viewModel: SaveListViewModel = hiltViewModel(),
) {
    ChangeStatusBarColor(main10)

    val navigateAction = LocalNavigateActionInterop.current
    val showSnackBar = rememberShowSnackBar()
    val savingListState by viewModel.savingListState.collectAsStateWithLifecycle()
    val challengeState by viewModel.challengeState.collectAsStateWithLifecycle()
    val month by viewModel.month.collectAsStateWithLifecycle()

    SavingListScreen(
        savingListState = savingListState,
        savingChallengeList = challengeState,
        month = month,
        onPrev = viewModel::prevMonth,
        onNext = viewModel::nextMonth,
        onShowDetail = navigateAction::navigateToSavingDetail,
        onSavingAdd = navigateAction::navigateToSavingAdd,
        onExecuteChange = viewModel::executeChange,
        onGoBack = navigateAction::popBackStack,
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
    savingChallengeList: List<SavingChallenge>,
    month: YearMonth,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onShowDetail: (Long) -> Unit,
    onSavingAdd: () -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
    onGoBack: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            Surface(
                shadowElevation = 4.dp,
            ) {
                RegularButton(
                    text = "추가",
                    color = main2,
                    onClick = onSavingAdd,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                )
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(main210),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                AcornBox(
                    count = (savingListState as? SavingListState.SavingListData)?.acornCount ?: 0,
                    goldCount = (savingListState as? SavingListState.SavingListData)?.goldAcornCount ?: 0,
                    flowerCount = savingChallengeList.size,
                )
                SavingListContent(
                    month = month,
                    onPrev = onPrev,
                    onNext = onNext,
                    savingListState = savingListState,
                    savingChallengeList = savingChallengeList,
                    onShowDetail = onShowDetail,
                    onExecuteChange = onExecuteChange,
                    modifier = Modifier.weight(1f),
                )
            }
            StateAnimatedVisibility<SavingListState.SavingListData>(
                target = savingListState,
                modifier = Modifier.align(Alignment.TopStart),
            ) {
                Column(
                    modifier = Modifier.padding(start = 30.dp, top = 60.dp),
                ) {
                    Text(
                        text = "전체 저축",
                        style = TypoTheme.typography.titleMediumM,

                    )
                    Text(
                        text = it.savePlanList.totalString,
                        style = TypoTheme.typography.displaySmallB,
                    )
                }
            }
            TopAppbarIcon(
                icon = Icons.Default.ArrowBackIosNew,
                onClick = onGoBack,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun SavingListContent(
    month: YearMonth,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    savingListState: SavingListState,
    savingChallengeList: List<SavingChallenge>,
    onShowDetail: (Long) -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    StateAnimatedVisibility<SavingListState.SavingListData>(
        target = savingListState,
        modifier = modifier,
    ) {
        SaveListBody(
            month = month,
            onPrev = onPrev,
            onNext = onNext,
            savePlanList = it.savePlanList,
            savingChallengeList = savingChallengeList,
            onShowDetail = onShowDetail,
            onExecuteChange = onExecuteChange,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingListScreenPreview() {
    JunTheme {
        SavingListScreen(
            savingListState = SavingListState.SavingListData(SavePlanList.sample),
            savingChallengeList = listOf(SavingChallenge.sample),
            month = YearMonth.now(),
            onPrev = {},
            onNext = {},
            onSavingAdd = {},
            onShowDetail = {},
            onGoBack = {},
            onExecuteChange = { _, _ -> },
        )
    }
}
