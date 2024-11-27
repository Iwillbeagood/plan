package jun.money.mate.save.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.TextSwitch
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Gray5
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.main
import jun.money.mate.model.save.SavePlan

@Composable
internal fun SaveListItem(
    savePlan: SavePlan,
    onClick: () -> Unit,
    onExecuteChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (savePlan.selected) main else Gray5
        ),
        color = MaterialTheme.colorScheme.surfaceDim,
        onClick = onClick,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = savePlan.dateString,
                    style = JUNTheme.typography.titleSmallB
                )
                Text(
                    text = savePlan.title,
                    style = JUNTheme.typography.titleSmallR,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = savePlan.amountString,
                    style = JUNTheme.typography.titleNormalB,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                VerticalSpacer(8.dp)
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextSwitch(
                        text = savePlan.saveState.name,
                        style = JUNTheme.typography.titleSmallB,
                        checked = savePlan.executed,
                        onCheckedChange = onExecuteChange
                    )
                }

            }
        }
    }
}

@Preview
@Composable
private fun SaveListItemPreview() {
    JunTheme {
        SaveListItem(
            savePlan = SavePlan(
                id = 0,
                title = "title",
                amount = 10000,
                planDay = 1,
                executeMonth = 1,
                executed = false,
                selected = false
            ),
            onExecuteChange = {},
            onClick = {}
        )
    }
}