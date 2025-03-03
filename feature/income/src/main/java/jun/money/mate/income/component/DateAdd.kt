package jun.money.mate.income.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem_date.datetimepicker.DatePicker
import jun.money.mate.designsystem_date.datetimepicker.DayPicker
import jun.money.mate.designsystem_date.datetimepicker.TimeBoundaries
import java.time.LocalDate

@Composable
fun DateAdd(
    onDaySelected: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    originIsMonthly: Boolean? = null
) {
    var isMonthly by remember { mutableStateOf(originIsMonthly) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TypeButton(
                text = "정기 수입",
                isType = isMonthly == true,
                onApplyType = {
                    isMonthly = true
                },
                modifier = Modifier.weight(1f)
            )
            HorizontalSpacer(10.dp)
            TypeButton(
                text = "단기 수입",
                isType = isMonthly == false,
                onApplyType = {
                    isMonthly = false
                },
                modifier = Modifier.weight(1f)
            )
        }
        VerticalSpacer(16.dp)
        Crossfade(
            isMonthly
        ) {
            when (it) {
                true -> {
                    DayPicker(
                        onDaySelected = onDaySelected,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                false -> {
                    DatePicker(
                        timeBoundary = TimeBoundaries.lastMonthToThisMonth,
                        onDateSelect = onDateSelected,
                    )
                }

                null -> {}
            }
        }
    }
}