package jun.money.mate.save.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem_date.datetimepicker.MonthBar
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.model.save.SavingChallenge
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun SaveListBody(
    month: YearMonth,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    savePlanList: SavePlanList,
    savingChallengeList: List<SavingChallenge>,
    onShowDetail: (Long) -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            VerticalSpacer(20.dp)
            MonthBar(
                month = month,
                onPrev = onPrev,
                onNext = onNext,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            VerticalSpacer(20.dp)
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(savingChallengeList) { savingChallenge ->
                    SavingChallengeItem(
                        savingChallenge = savingChallenge,
                    )
                }
                items(savePlanList.savePlans) { savePlan ->
                    SaveListItem(
                        savePlan = savePlan,
                        onClick = onShowDetail,
                        onExecuteChange = { onExecuteChange(it, savePlan.id) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveListBodyPreview() {
    JunTheme {
        SaveListBody(
            month = YearMonth.now(),
            onPrev = {},
            onNext = {},
            savePlanList = SavePlanList.sample,
            savingChallengeList = listOf(SavingChallenge.sample),
            onShowDetail = {},
            onExecuteChange = { _, _ -> },
        )
    }
}