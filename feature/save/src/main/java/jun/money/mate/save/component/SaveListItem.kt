package jun.money.mate.save.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.Label
import jun.money.mate.designsystem.component.DefaultSwitch
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray3
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red2
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavingsType.Companion.title
import jun.money.mate.ui.SeedIcon
import jun.money.mate.utils.toRemainingDayString

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SaveListItem(
    savePlan: SavePlan,
    onClick: (Long) -> Unit,
    onExecuteChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .shadow(2.dp, shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = { onClick(savePlan.id) },
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SeedIcon(
                modifier = Modifier.size(18.dp)
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
                            text = savePlan.savingsType.title,
                            style = TypoTheme.typography.titleMediumM,
                        )
                        Text(
                            text = "${savePlan.day}일",
                            style = TypoTheme.typography.titleSmallR,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = savePlan.amountString,
                        style = TypoTheme.typography.titleNormalB,
                        textAlign = TextAlign.End
                    )
                }
                VerticalSpacer(6.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val period = savePlan.getRemainingPeriod()
                    if (period == null) {
                        HorizontalSpacer(1f)
                        DefaultSwitch(
                            checked = savePlan.executed,
                            onCheckedChange = onExecuteChange
                        )
                    } else {
                        Label(
                            text = period,
                            color = Gray3,
                        )
                        HorizontalSpacer(4.dp)
                        Label(
                            text = savePlan.day.toRemainingDayString(),
                            color = Red2,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContinueSaveListItemPreview() {
    JunTheme {
        SaveListItem(
            savePlan = SavePlan.sample,
            onClick = {},
            onExecuteChange = {},
        )
    }
}