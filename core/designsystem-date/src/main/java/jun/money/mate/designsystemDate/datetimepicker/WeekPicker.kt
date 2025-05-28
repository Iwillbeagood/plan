package jun.money.mate.designsystemDate.datetimepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.Blue1
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Red2
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.utils.toKorean
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun WeekPicker(
    onWeekSelected: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier,
    selectedDayOfWeek: DayOfWeek = LocalDate.now().dayOfWeek,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        DayOfWeekButton(
            dayOfWeek = DayOfWeek.MONDAY,
            color = Red2,
            selectedDayOfWeek = selectedDayOfWeek,
            onDayOfWeekSelected = onWeekSelected,
            modifier = Modifier.weight(1f),
        )
        DayOfWeekButton(
            dayOfWeek = DayOfWeek.TUESDAY,
            selectedDayOfWeek = selectedDayOfWeek,
            onDayOfWeekSelected = onWeekSelected,
            modifier = Modifier.weight(1f),
        )
        DayOfWeekButton(
            dayOfWeek = DayOfWeek.WEDNESDAY,
            selectedDayOfWeek = selectedDayOfWeek,
            onDayOfWeekSelected = onWeekSelected,
            modifier = Modifier.weight(1f),
        )
        DayOfWeekButton(
            dayOfWeek = DayOfWeek.THURSDAY,
            selectedDayOfWeek = selectedDayOfWeek,
            onDayOfWeekSelected = onWeekSelected,
            modifier = Modifier.weight(1f),
        )
        DayOfWeekButton(
            dayOfWeek = DayOfWeek.FRIDAY,
            selectedDayOfWeek = selectedDayOfWeek,
            onDayOfWeekSelected = onWeekSelected,
            modifier = Modifier.weight(1f),
        )
        DayOfWeekButton(
            dayOfWeek = DayOfWeek.SATURDAY,
            selectedDayOfWeek = selectedDayOfWeek,
            onDayOfWeekSelected = onWeekSelected,
            modifier = Modifier.weight(1f),
        )
        DayOfWeekButton(
            dayOfWeek = DayOfWeek.SUNDAY,
            selectedDayOfWeek = selectedDayOfWeek,
            color = Blue1,
            onDayOfWeekSelected = onWeekSelected,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun DayOfWeekButton(
    dayOfWeek: DayOfWeek,
    selectedDayOfWeek: DayOfWeek,
    color: Color = Black,
    onDayOfWeekSelected: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(5.dp),
        onClick = { onDayOfWeekSelected(dayOfWeek) },
        color = if (dayOfWeek == selectedDayOfWeek) MaterialTheme.colorScheme.primary else White1,
        border = BorderStroke(1.dp, if (dayOfWeek == selectedDayOfWeek) MaterialTheme.colorScheme.primary else Gray6),
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 30.dp),
        ) {
            Text(
                text = dayOfWeek.toKorean(),
                style = TypoTheme.typography.titleNormalB,
                textAlign = TextAlign.Center,
                color = if (dayOfWeek == selectedDayOfWeek) White1 else color,
            )
        }
    }
}

@Preview
@Composable
private fun WeekPickerPreview() {
    JunTheme {
        WeekPicker(
            onWeekSelected = {},
        )
    }
}
