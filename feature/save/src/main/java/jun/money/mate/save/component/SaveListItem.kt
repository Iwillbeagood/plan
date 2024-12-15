package jun.money.mate.save.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.TextSwitch
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Orange1
import jun.money.mate.model.save.SavePlan

@Composable
internal fun SaveListItem(
    savePlan: SavePlan,
    onExecuteChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = savePlan.title,
                    style = JUNTheme.typography.titleNormalM
                )
                Text(
                    text = savePlan.dateString + " | "+ savePlan.amountString,
                    style = JUNTheme.typography.titleSmallM,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSwitch(
                text = savePlan.saveState.name,
                style = JUNTheme.typography.titleSmallB,
                checkedColor = Orange1,
                checked = savePlan.executed,
                onCheckedChange = onExecuteChange
            )
        }
    }
}
/**
 * 목표서 타입일 경우에는 목표를 보여주고 추가적으로 남은 달을 보여줄 것.
 * */

@Preview(showBackground = true)
@Composable
private fun ContinueSaveListItemPreview() {
    JunTheme {
        SaveListItem(
            savePlan = SavePlan.sample,
            onExecuteChange = {},
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PlanSaveListItemPreview() {
    JunTheme {
        SaveListItem(
            savePlan = SavePlan.sample2,
            onExecuteChange = {},
        )
    }
}