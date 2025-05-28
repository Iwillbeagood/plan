package jun.money.mate.challenge.component

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
import jun.money.mate.designsystemDate.datetimepicker.DayPicker
import jun.money.mate.designsystemDate.datetimepicker.WeekPicker
import jun.money.mate.model.save.ChallengeType
import jun.money.mate.ui.TypeButton
import java.time.LocalDate

@Composable
fun ChallengeDateType(
    onChallengeTypeSelected: (ChallengeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMonthly by remember { mutableStateOf<Boolean?>(null) }
    var selectedWeek by remember { mutableStateOf(LocalDate.now().dayOfWeek) }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            TypeButton(
                text = "한달마다",
                isType = isMonthly == true,
                onApplyType = {
                    isMonthly = true
                },
                modifier = Modifier.weight(1f),
            )
            HorizontalSpacer(10.dp)
            TypeButton(
                text = "일주일마다",
                isType = isMonthly == false,
                onApplyType = {
                    isMonthly = false
                },
                modifier = Modifier.weight(1f),
            )
        }
        VerticalSpacer(16.dp)
        Crossfade(
            isMonthly,
        ) {
            when (it) {
                true -> {
                    DayPicker(
                        onDaySelected = { day ->
                            onChallengeTypeSelected(ChallengeType.Monthly(day.toInt()))
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                false -> {
                    WeekPicker(
                        onWeekSelected = { week ->
                            selectedWeek = week
                            onChallengeTypeSelected(ChallengeType.Weekly(week))
                        },
                        selectedDayOfWeek = selectedWeek,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                else -> {}
            }
        }
    }
}
