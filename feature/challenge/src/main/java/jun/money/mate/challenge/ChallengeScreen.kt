package jun.money.mate.challenge

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
import jun.money.mate.challenge.component.ChallengeLazyColumn
import jun.money.mate.challenge.contract.ChallengeEffect
import jun.money.mate.challenge.contract.ChallengeState
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.StateAnimatedVisibility
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray4
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.save.Challenge
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.utils.currency.CurrencyFormatter

@Composable
internal fun ChallengeRoute(
    viewModel: ChallengeViewModel = hiltViewModel(),
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.surface)

    val navigateAction = LocalNavigateActionInterop.current
    val showSnackBar = rememberShowSnackBar()
    val challengeState by viewModel.challengeState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppbar(
                onBackEvent = navigateAction::popBackStack,
            )
        },
        bottomBar = {
            val state = challengeState
            if (state is ChallengeState.ChallengeData && !state.challenge.challengeCompleted) {
                RegularButton(
                    text = "챌린지 포기하기",
                    onClick = viewModel::giveUpChallenge,
                    color = Gray4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            ChallengeContent(
                challengeState = challengeState,
                onAchieveChange = viewModel::changeAchieve,
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.challengeEffect.collect { effect ->
            when (effect) {
                is ChallengeEffect.EditSpendingPlan -> navigateAction.navigateToSavingDetail(effect.id)
                is ChallengeEffect.ShowSnackBar -> showSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun ChallengeContent(
    challengeState: ChallengeState,
    onAchieveChange: (Boolean, Long) -> Unit,
) {
    StateAnimatedVisibility<ChallengeState.ChallengeData>(
        target = challengeState,
    ) {
        ChallengeScreen(
            challengeState = it,
            onAchieveChange = onAchieveChange,
        )
    }
}

@Composable
private fun ChallengeScreen(
    challengeState: ChallengeState.ChallengeData,
    onAchieveChange: (Boolean, Long) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        VerticalSpacer(40.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 30.dp),
        ) {
            Column {
                Text(
                    text = "챌린지 금액",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = TypoTheme.typography.headlineSmallM,
                )
                Text(
                    text = CurrencyFormatter.formatAmountWon(challengeState.challenge.goalAmount),
                    style = TypoTheme.typography.displaySmallB,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = challengeState.challenge.achievedCount,
                    style = TypoTheme.typography.displaySmallB,
                    color = MaterialTheme.colorScheme.primary,
                )
                HorizontalSpacer(2.dp)
                Text(
                    text = challengeState.challenge.totalTimes(),
                    style = TypoTheme.typography.titleLargeM,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        VerticalSpacer(30.dp)
        ChallengeLazyColumn(
            challenge = challengeState.challenge,
            onAchieveChange = onAchieveChange,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingListScreenPreview() {
    JunTheme {
        ChallengeScreen(
            challengeState = ChallengeState.ChallengeData(Challenge.sample),
            onAchieveChange = { _, _ -> },
        )
    }
}
