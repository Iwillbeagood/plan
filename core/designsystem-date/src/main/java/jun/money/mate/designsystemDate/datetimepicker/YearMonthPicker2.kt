package jun.money.mate.designsystemDate.datetimepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystemDate.datetimepicker.views.Picker
import jun.money.mate.designsystemDate.datetimepicker.views.TimeFormat
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun YearMonthPicker(
    onDateSelected: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    initialYear: Int = YearMonth.now().year,
    initialMonth: Int = YearMonth.now().monthValue,
) {
    val currentYear = remember { mutableIntStateOf(initialYear) }
    val currentMonth = remember { mutableIntStateOf(initialMonth) }

    val years = ((LocalDate.now().year - 3)..LocalDate.now().year).map(Int::toString).toImmutableList()
    val months = (1..12).map(Int::toString).toImmutableList()

    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, Gray6),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Picker(
                    texts = years,
                    count = years.size,
                    rowCount = 5,
                    size = DpSize(100.dp, 150.dp),
                    startIndex = years.indexOf(currentYear.value.toString()),
                    onItemSelected = { selectedYear ->
                        currentYear.value = selectedYear.toInt()
                        onDateSelected(currentYear.value, currentMonth.value)
                    },
                    timeFormat = TimeFormat.YEAR,
                    modifier = Modifier.weight(1f),
                )

                Picker(
                    texts = months,
                    count = months.size,
                    rowCount = 5,
                    size = DpSize(100.dp, 150.dp),
                    startIndex = months.indexOf(currentMonth.value.toString()),
                    onItemSelected = { selectedMonth ->
                        currentMonth.value = selectedMonth.toInt()
                        onDateSelected(currentYear.value, currentMonth.value)
                    },
                    timeFormat = TimeFormat.MONTH,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun YearMonthPreview() {
    JunTheme {
        YearMonthPicker(
            onDateSelected = { _, _ -> },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
