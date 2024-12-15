package jun.money.mate.save.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavePlanList

@Composable
internal fun SaveListBody(
    savePlanList: SavePlanList,
    onSavePlanClick: (SavePlan) -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
) {
    Column {
        Surface(
            color = MaterialTheme.colorScheme.surfaceDim,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 40.dp),
            ) {
                Row {
                    Text(
                        text = "저금 계획",
                        style = JUNTheme.typography.titleLargeM,
                    )
                    Text(
                        text = savePlanList.totalString,
                        style = JUNTheme.typography.headlineSmallB,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row {
                    Text(
                        text = "실천 금액",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = JUNTheme.typography.titleMediumM,
                    )
                    Text(
                        text = savePlanList.executedTotalString,
                        style = JUNTheme.typography.titleNormalM,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        VerticalSpacer(20.dp)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(savePlanList.savePlans) { savePlan ->
                SaveListItem(
                    savePlan = savePlan,
                    onExecuteChange = { onExecuteChange(it, savePlan.id) },
                    modifier = Modifier
                        .clickable {
                            onSavePlanClick(savePlan)
                        },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveListBodyPreview() {
    JunTheme {
        SaveListBody(
            savePlanList = SavePlanList.sample,
            onSavePlanClick = {},
            onExecuteChange = { _, _ -> },
        )
    }
}