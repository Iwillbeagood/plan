package jun.money.mate.finance.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.Gray7
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red2
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.model.save.ChallengeType.Companion.dayString
import jun.money.mate.model.save.MoneyChallenge

@Composable
internal fun MoneyChallengeLazyColumn(
    moneyChallengeList: List<MoneyChallenge>,
    onAddClick: () -> Unit,
    onChallengeClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth(),
    ) {
        items(moneyChallengeList) {
            MoneyChallengeItem(
                moneyChallenge = it,
                onClick = {
                    onChallengeClick(it.id)
                },
            )
        }
        item {
            Column {
                VerticalSpacer(4.dp)
                PlusButton(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}

@Composable
private fun MoneyChallengeItem(
    moneyChallenge: MoneyChallenge,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Gray6),
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
                        text = moneyChallenge.title,
                        style = TypoTheme.typography.titleLargeB,
                    )
                    VerticalSpacer(16.dp)
                    Text(
                        text = moneyChallenge.type.dayString(),
                        style = TypoTheme.typography.titleNormalM,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    if (!moneyChallenge.challengeCompleted) {
                        Text(
                            text = "다음 결제일 : ${moneyChallenge.nextDate}",
                            style = TypoTheme.typography.titleSmallR,
                            color = Red2,
                            modifier = Modifier.padding(top = 4.dp)
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
                            text = moneyChallenge.achievedCount,
                            style = TypoTheme.typography.headlineMediumB,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalSpacer(2.dp)
                        Text(
                            text = moneyChallenge.totalTimes(),
                            style = TypoTheme.typography.titleNormalM,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            CompleteMark(
                visible = moneyChallenge.challengeCompleted
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
            text = "도전 성공!",
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

@Composable
private fun PlusButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primary,
        onClick = onClick,
        shadowElevation = 2.dp,
        modifier = modifier.padding(16.dp),
    ) {
        Text(
            text = "챌린지 시작하기",
            style = TypoTheme.typography.titleMediumB,
            color = White1,
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MoneyChallengePreview() {
    JunTheme {
        MoneyChallengeLazyColumn(
            moneyChallengeList = listOf(MoneyChallenge.sample),
            onAddClick = {},
            onChallengeClick = {}
        )
    }
}