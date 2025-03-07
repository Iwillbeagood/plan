package jun.money.mate.designsystem_date.datetimepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.main
import jun.money.mate.designsystem_date.datetimepicker.views.Picker
import jun.money.mate.designsystem_date.datetimepicker.views.TimeFormat
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate

@Composable
fun PeriodPicker(
    onPeriodSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isYearSelected by remember { mutableStateOf(true) }
    val period = (1..11).map { "$it" }.toImmutableList()

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row {
            Spacer(modifier = Modifier.width(6.dp))
            YearMonthButton(
                text = "년",
                isSelected = isYearSelected,
                onClick = { isYearSelected = true }
            )
            Spacer(modifier = Modifier.width(10.dp))
            YearMonthButton(
                text = "개월",
                isSelected = !isYearSelected,
                onClick = { isYearSelected = false }
            )
        }
        VerticalSpacer(10.dp)
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 4.dp,
            border = BorderStroke(1.dp, Gray6),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Picker(
                    texts = period,
                    count = period.size,
                    rowCount = 5,
                    size = DpSize(100.dp, 150.dp),
                    startIndex = 0,
                    onItemSelected = {
                        onPeriodSelected(
                            if (isYearSelected) {
                                it.toInt() * 12
                            } else {
                                it.toInt()
                            }
                        )
                    },
                    timeFormat = if (isYearSelected) TimeFormat.YEAR else TimeFormat.MONTH_PERIOD,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun YearMonthButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        color = if (isSelected) main else Color.White,
        shadowElevation = if (isSelected) 0.dp else 4.dp,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Black,
            style = TypoTheme.typography.titleLargeB,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Preview
@Composable
private fun PeriodPickerPreview() {
    JunTheme {
        PeriodPicker(
            onPeriodSelected = { }
        )
    }
}