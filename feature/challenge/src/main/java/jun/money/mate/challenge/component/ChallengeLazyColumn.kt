package jun.money.mate.challenge.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.DefaultSwitch
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.Gray4
import jun.money.mate.designsystem.theme.Gray7
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.save.Challenge
import jun.money.mate.model.save.ChallengeProgress
import jun.money.mate.model.save.ChallengeProgressType
import jun.money.mate.model.save.ChallengeProgressType.Now
import jun.money.mate.model.save.ChallengeProgressType.PAST
import jun.money.mate.model.save.ChallengeProgressType.UPCOMING
import jun.money.mate.ui.FlowerImage
import jun.money.mate.utils.formatDateBasedOnYear

data class ChallengeItemParam(
    val iconSize: Int = 32,
    val textColor: Color = Gray4,
)

@Composable
internal fun ChallengeLazyColumn(
    challenge: Challenge,
    onAchieveChange: (Boolean, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val nextIndex = challenge.nextProgress?.index
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        itemsIndexed(challenge.progress) { index, item ->
            ChallengeItem(
                challengeProgress = item,
                challengeProgressType = when {
                    nextIndex == null -> PAST
                    index + 1 == nextIndex -> Now
                    index + 1 < nextIndex -> PAST
                    else -> UPCOMING
                },
                onAchieveChange = { onAchieveChange(it, item.id) },
                modifier = modifier
            )
        }
        item {
            VerticalSpacer(8.dp)
        }
    }
}

@Composable
private fun ChallengeItem(
    challengeProgress: ChallengeProgress,
    challengeProgressType: ChallengeProgressType,
    onAchieveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val param = when (challengeProgressType) {
        Now -> ChallengeItemParam(
            textColor = Black,
            iconSize = 48
        )
        PAST -> ChallengeItemParam()
        UPCOMING -> ChallengeItemParam()
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceDim,
        border = BorderStroke(1.dp, Gray7),
        shadowElevation = 2.dp,
        modifier = modifier
            .padding(vertical = if (challengeProgressType == Now) 16.dp else 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            FlowerImage(
                size = param.iconSize,
                isAchieved = challengeProgress.isAchieved,
                modifier = Modifier.padding(10.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${challengeProgress.index}회차",
                    style = if (challengeProgress.isAchieved)
                        TypoTheme.typography.titleLargeB
                    else
                        TypoTheme.typography.titleMediumM,
                    color = param.textColor
                )
                Text(
                    text = formatDateBasedOnYear(challengeProgress.date),
                    style = TypoTheme.typography.titleSmallM,
                    color = param.textColor
                )
            }
            if (challengeProgressType == Now) {
                DefaultSwitch(
                    checked = challengeProgress.isAchieved,
                    onCheckedChange = onAchieveChange,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChallengeLazyColumnPreview() {
    JunTheme {
        ChallengeLazyColumn(
            challenge = Challenge.sample,
            onAchieveChange = { _, _ -> }
        )
    }
}