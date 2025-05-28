package jun.money.mate.cost.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.designsystem.theme.nonScaledSp
import jun.money.mate.model.spending.Cost
import jun.money.mate.utils.toImageRes
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.ceil

data class CostCalendarValue(
    val date: Int,
    val costs: List<Cost>,
    val selected: Boolean = false,
)

@Composable
internal fun CostCalendar(
    costCalendarValue: List<CostCalendarValue>,
    selectedCalendarValue: CostCalendarValue?,
    onSelectedCalendarValue: (CostCalendarValue?) -> Unit,
) {
    val currentMonth = LocalDate.now().month
    val currentYear = LocalDate.now().year

    val firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1)
    val totalDaysInMonth = firstDayOfMonth.lengthOfMonth()
    val daysInWeek = 7
    val koreanLocale = Locale("ko", "KR")

    val startDay = firstDayOfMonth.dayOfWeek.ordinal
    val totalRows = ceil((startDay + totalDaysInMonth) / daysInWeek.toFloat()).toInt()

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(daysInWeek),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .heightIn(max = 350.dp),
        ) {
            items(daysInWeek) { index ->
                val dayOfWeek = LocalDate.now().plusDays(index.toLong()).dayOfWeek
                Text(
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, koreanLocale),
                    style = TypoTheme.typography.titleMediumB.nonScaledSp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
                )
            }
            items(totalRows * daysInWeek) { index ->
                val currentDayIndex = index - startDay
                if (currentDayIndex in 0 until totalDaysInMonth) {
                    val currentDate = firstDayOfMonth.plusDays(currentDayIndex.toLong())
                    val item = costCalendarValue.find { it.date == currentDate.dayOfMonth }
                    DayCell(
                        date = currentDate,
                        selected = item != null && selectedCalendarValue == item,
                        costCalendarValue = item,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable(
                                enabled = item != null,
                                onClick = {
                                    if (item != null && item != selectedCalendarValue) {
                                        onSelectedCalendarValue(item)
                                    } else {
                                        onSelectedCalendarValue(null)
                                    }
                                },
                            ),
                    )
                } else {
                    // 달력의 빈 공간을 채우기 위해 Spacer 사용
                    Spacer(modifier = Modifier.width(40.dp))
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    selected: Boolean,
    costCalendarValue: CostCalendarValue?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else
                    MaterialTheme.colorScheme.surface,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = if (date == LocalDate.now()) {
                TypoTheme.typography.titleMediumB.nonScaledSp
            } else
                TypoTheme.typography.titleMediumM.nonScaledSp,
            color = if (selected) {
                White1
            } else if (date == LocalDate.now()) {
                MaterialTheme.colorScheme.primary
            } else
                MaterialTheme.colorScheme.onSurfaceVariant,
        )
        VerticalSpacer(2.dp)
        if (costCalendarValue != null) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                val size = costCalendarValue.costs.size
                if (size > 2) {
                    Text(
                        text = "${size}개",
                        style = TypoTheme.typography.titleMediumM.nonScaledSp,
                        color = if (selected) {
                            White1
                        } else
                            MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(2.dp),
                    )
                } else {
                    costCalendarValue.costs.forEach { cost ->
                        Icon(
                            painter = painterResource(
                                cost.costType.toImageRes(),
                            ),
                            tint = Color.Unspecified,
                            contentDescription = "Spending Icon",
                            modifier = Modifier
                                .padding(2.dp)
                                .size(16.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    JunTheme {
        CostCalendar(
            costCalendarValue = listOf(
                CostCalendarValue(
                    10,
                    Cost.samples,
                ),
            ),
            selectedCalendarValue = null,
            onSelectedCalendarValue = {},
        )
    }
}
