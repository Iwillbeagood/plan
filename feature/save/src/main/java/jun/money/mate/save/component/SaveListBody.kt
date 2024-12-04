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
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.ScrollableTab
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.model.save.SaveType

/***
 * 저금 목표 금액과 실제 저금 금액이 나타날 것. - 해당 여부 금액은 실행되었는지, 실행되지 않았는지로 판단함
 * 저금 타입별로 분류해서 보여줌. 필터는 굳이 필요없을 것 같음.
 * 저금은 앵간하면 은행이기에 은행 아이콘과 함께 보여줄 것.
 *
 * 저금을 했는지, 안했는지 토글을 통해 변경할 수 있음.
 * 일단 전체적으로 개발을 완료한 다음에 해당 날짜에 실제로 저금을 진행했는지 변경
 */
@Composable
internal fun SaveListBody(
    selectedSaveType: SaveType,
    savePlanList: SavePlanList,
    onSavePlanClick: (SavePlan) -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
    onTabClick: (Int) -> Unit,
) {
    Column {
        VerticalSpacer(20.dp)
        Surface(
            color = MaterialTheme.colorScheme.surfaceDim,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
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
        HorizontalDivider(thickness = 8.dp)
        VerticalSpacer(20.dp)

        ScrollableTab(
            tabs = SaveType.entries.map { it.title },
            selectedTabIndex = selectedSaveType.ordinal,
            onTabClick = onTabClick,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                when (selectedSaveType) {
                    SaveType.PlaningSave -> savePlanList.planingPlans
                    SaveType.ContinueSave -> savePlanList.continuePlans
                }
            ) { savePlan ->
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
            selectedSaveType = SaveType.ContinueSave,
            savePlanList = SavePlanList.sample,
            onSavePlanClick = {},
            onExecuteChange = { _, _ -> },
            onTabClick = {},
        )
    }
}