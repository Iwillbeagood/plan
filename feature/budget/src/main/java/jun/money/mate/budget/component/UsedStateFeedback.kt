package jun.money.mate.budget.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray5
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem.theme.main
import jun.money.mate.model.consumption.BudgetUsedState
import jun.money.mate.utils.currency.CurrencyFormatter

@Composable
internal fun UsedStateFeedback(
    usedState: BudgetUsedState,
    maxUse: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (usedState == BudgetUsedState.NormalUsed) return

    TopToBottomAnimatedVisibility(1500) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .border(1.dp, Gray5, RoundedCornerShape(12.dp)),
            shadowElevation = 2.dp,
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            color = White1,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    tint = main,
                    contentDescription = null,
                )
                HorizontalSpacer(10.dp)
                Column {
                    Text(
                        text = when (usedState) {
                            is BudgetUsedState.OverUsed -> "지금까지 예산을 ${usedState.overBudgetCount}번 초과했어요.\n다음 달엔 조금 더 여유롭게 잡아볼까요?"
                            BudgetUsedState.UnderUsed -> "멋진 절약 습관이에요!\n예산을 살짝 줄여봐도 괜찮지 않을까요?"
                            else -> ""
                        },
                        style = TypoTheme.typography.titleSmallM,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    VerticalSpacer(6.dp)
                    Row {
                        Text(
                            text = "가장 많이 사용한 금액은",
                            style = TypoTheme.typography.titleSmallM,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "${CurrencyFormatter.formatToWon(maxUse)}원",
                            style = TypoTheme.typography.titleSmallB,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "이에요.",
                            style = TypoTheme.typography.titleSmallM,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedbackPreview() {
    JunTheme {
        Column {
            UsedStateFeedback(
                usedState = BudgetUsedState.OverUsed(4),
                onClick = {},
                maxUse = 1000000L,
            )
        }
    }
}
