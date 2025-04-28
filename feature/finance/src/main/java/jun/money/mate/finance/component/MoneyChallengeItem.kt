package jun.money.mate.finance.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.Scrim
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red2
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.save.Challenge
import jun.money.mate.model.save.ChallengeProgress
import jun.money.mate.model.save.ChallengeType
import jun.money.mate.model.save.ChallengeType.Companion.dayString
import jun.money.mate.utils.currency.CurrencyFormatter
import jun.money.mate.utils.formatDateBasedOnYear
import java.time.LocalDate

@Composable
internal fun MoneyChallengeItem(
    challenge: Challenge,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clickable(onClick = onClick)
                    .padding(vertical = 20.dp, horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Text(
                        text = challenge.title.ifBlank {
                            CurrencyFormatter.formatToWon(challenge.goalAmount) + " 챌린지"
                        },
                        style = TypoTheme.typography.titleLargeB,
                    )
                    VerticalSpacer(16.dp)
                    Text(
                        text = challenge.type.dayString(),
                        style = TypoTheme.typography.titleMediumM,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    val nextDate = challenge.nextDate
                    if (!challenge.challengeCompleted && nextDate != null) {
                        Text(
                            text = formatDateBasedOnYear(nextDate),
                            style = TypoTheme.typography.titleSmallM,
                            color = Red2,
                        )
                    }
                    VerticalSpacer(1f)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = challenge.achievedCount,
                            style = TypoTheme.typography.headlineMediumB,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalSpacer(2.dp)
                        Text(
                            text = challenge.totalTimes(),
                            style = TypoTheme.typography.titleSmallM,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            CompleteMark(
                visible = challenge.challengeCompleted
            )
        }
    }
}

@Composable
private fun BoxScope.CompleteMark(
    visible: Boolean
) {
    Scrim(
        visible = visible
    )
    if (visible) {
        Text(
            text = "챌린지 성공!",
            style = TypoTheme.typography.headlineMediumB,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .graphicsLayer(
                    rotationZ = -15f
                )
                .align(Alignment.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MoneyChallengePreview() {
    JunTheme {
        MoneyChallengeItem(
            challenge = Challenge(
                id = 0,
                title = "테스트",
                count = 10,
                startDate = LocalDate.now(),
                goalAmount = 1000000,
                type = ChallengeType.Monthly(1),
                progress = listOf(
                    ChallengeProgress(
                        id = 0,
                        index = 0,
                        challengeId = 0,
                        amount = 100000,
                        date = LocalDate.now(),
                        isAchieved = false
                    )
                )
            ),
            onClick = {}
        )
    }
}