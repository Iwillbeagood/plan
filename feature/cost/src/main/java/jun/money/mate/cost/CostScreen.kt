package jun.money.mate.cost

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import jun.money.mate.cost.contract.CostEffect
import jun.money.mate.challenge.contract.CostState
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray4
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.save.Challenge
import jun.money.mate.ui.interop.LocalNavigateActionInterop
import jun.money.mate.ui.interop.rememberShowSnackBar
import jun.money.mate.utils.currency.CurrencyFormatter

/**
 * 일단 이미 지나간 것들에 대해서는 날짜와 스위치가 보여야 함.
 * 현재 체크해야하는 것도 날짜와 스위치가 보여야하고, 강조 표시가 되어야 할듯
 * 아직 오지 않는 것들에 대해서는 날짜만 보여야 할듯
 * 가장 상단에는 목표 금액이 있으면 될듯.
 *
 * 목표 기간 내에
 * */
@Composable
internal fun CostRoute(
    viewModel: CostViewModel = hiltViewModel()
) {
    ChangeStatusBarColor()

    val navigateAction = LocalNavigateActionInterop.current
    val showSnackBar = rememberShowSnackBar()
    val challengeState by viewModel.costState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppbar(
                onBackEvent = navigateAction::popBackStack
            )
        },
        bottomBar = {
            val state = challengeState
            if (state is CostState.CostData && !state.challenge.challengeCompleted) {
                RegularButton(
                    text = "챌린지 포기하기",
                    onClick = viewModel::giveUpChallenge,
                    color = Gray4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ChallengeContent(
                costState = challengeState,
                onAchieveChange = viewModel::changeAchieve
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.costEffect.collect { effect ->
            when (effect) {
                is CostEffect.EditSpendingPlan -> navigateAction.navigateToSavingDetail(effect.id)
                is CostEffect.ShowSnackBar -> showSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun ChallengeContent(
    costState: CostState,
    onAchieveChange: (Boolean, Long) -> Unit,
) {
    FadeAnimatedVisibility(
        costState is CostState.CostData,
    ) {
        if (costState is CostState.CostData) {
            ChallengeScreen(
                costState = costState,
                onAchieveChange = onAchieveChange,
            )
        }
    }
}

@Composable
private fun ChallengeScreen(
    costState: CostState.CostData,
    onAchieveChange: (Boolean, Long) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VerticalSpacer(40.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 30.dp)
        ) {
            Column {
                Text(
                    text = "챌린지 금액",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = TypoTheme.typography.headlineSmallM,
                )
                Text(
                    text = CurrencyFormatter.formatAmountWon(costState.challenge.goalAmount),
                    style = TypoTheme.typography.displaySmallB,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = costState.challenge.achievedCount,
                    style = TypoTheme.typography.displaySmallB,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalSpacer(2.dp)
                Text(
                    text = costState.challenge.totalTimes(),
                    style = TypoTheme.typography.titleLargeM,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        VerticalSpacer(30.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingListScreenPreview() {
    JunTheme {
        ChallengeScreen(
            costState = CostState.CostData(Challenge.sample),
            onAchieveChange = { _, _ -> }
        )
    }
}