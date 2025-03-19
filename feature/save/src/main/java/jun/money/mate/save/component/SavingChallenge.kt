package jun.money.mate.save.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.Utils
import jun.money.mate.model.save.SavingChallenge
import jun.money.mate.ui.FlowerImage

@Composable
internal fun SavingChallengeItem(
    savingChallenge: SavingChallenge,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .shadow(2.dp, shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceDim, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            FlowerImage(
                size = 18,
                isAchieved = true,
            )
            HorizontalSpacer(6.dp)
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = savingChallenge.title,
                            style = TypoTheme.typography.titleMediumM,
                        )
                        Text(
                            text = "${savingChallenge.day}일",
                            style = TypoTheme.typography.titleSmallR,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = Utils.formatAmountWon(savingChallenge.amount),
                        style = TypoTheme.typography.titleNormalB,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SavingChallengeItemPreview() {
    JunTheme {
        SavingChallengeItem(
            savingChallenge = SavingChallenge.sample
        )
    }
}