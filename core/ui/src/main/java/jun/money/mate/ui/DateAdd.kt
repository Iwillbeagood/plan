package jun.money.mate.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.designsystem_date.datetimepicker.DayPicker
import jun.money.mate.designsystem_date.datetimepicker.TimeBoundaries
import jun.money.mate.model.etc.DateType
import java.time.LocalDate

@Composable
fun DateAdd(
    type: String,
    onDateSelected: (Int) -> Unit,
    onDateTypeSelected: (DateType) -> Unit,
    dateType: DateType?,
    date: Int = LocalDate.now().dayOfMonth,
    disableTypeChange: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (!disableTypeChange) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TypeButton(
                    text = "정기 $type",
                    isType = dateType == DateType.Monthly,
                    onApplyType = {
                        onDateTypeSelected(DateType.Monthly)
                    },
                    modifier = Modifier.weight(1f),
                )
                HorizontalSpacer(10.dp)
                TypeButton(
                    text = "단기 $type",
                    isType = dateType == DateType.Specific,
                    onApplyType = {
                        onDateTypeSelected(DateType.Specific)
                    },
                    modifier = Modifier.weight(1f),
                )
            }
            VerticalSpacer(16.dp)
        }
        Crossfade(
            dateType,
        ) {
            when (it) {
                DateType.Monthly -> {
                    DayPicker(
                        onDaySelected = { onDateSelected(it.toInt()) },
                        modifier = Modifier.fillMaxWidth(),
                        selectedDay = date.toString(),
                    )
                }
                DateType.Specific -> {
                    DatePicker(
                        timeBoundary = TimeBoundaries.lastMonthToThisMonth,
                        onDateSelect = { onDateSelected(it.dayOfMonth) },
                        selectedDate = LocalDate.of(LocalDate.now().year, LocalDate.now().monthValue, date),
                    )
                }

                null -> {}
            }
        }
    }
}
