package jun.money.mate.designsystem_date.datetimepicker.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.Black
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.main
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    startTime: LocalTime = LocalTime.now(),
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX,
    rowCount: Int = 3,
    onSnappedTime: (snappedTime: LocalTime) -> Unit = {},
) {
    var isAmPm by remember { mutableStateOf(
        if (startTime.isAfter(LocalTime.NOON)) AmPmValue.PM else AmPmValue.AM
    ) }

    var snappedTime by remember { mutableStateOf(startTime.truncatedTo(ChronoUnit.MINUTES)) }

    val hours = (1..12).map {
        Hour(
            text = it.toString(),
            value = it,
            index = it - 1
        )
    }

    val minutes = (0..59).map {
        Minute(
            text = it.toString(),
            value = it,
            index = it
        )
    }

    Column {
        Row {
            HmAmPmButton(
                text = "오전",
                isSelected = isAmPm == AmPmValue.AM,
                onClick = {
                    isAmPm = AmPmValue.AM
                    onSnappedTime(snappedTime.withHour(localTimeToAmPmHour(snappedTime)))
                }
            )
            Spacer(modifier = Modifier.width(20.dp))
            HmAmPmButton(
                text = "오후",
                isSelected = isAmPm == AmPmValue.PM,
                onClick = {
                    isAmPm = AmPmValue.PM
                    onSnappedTime(snappedTime.withHour(localTimeToAmPmHour(snappedTime)))
                }
            )
        }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                //Hour
                HmTextPicker(
                    texts = hours.map { it.text }.toImmutableList(),
                    rowCount = rowCount,
                    startIndex = hours.find { it.value ==  localTimeToAmPmHour(startTime) }?.index?.plus(60) ?: 12,
                    onItemSelected = { snappedText ->

                        val newHour = amPmHourToHour24(
                            hours.find { it.text == snappedText }?.value ?: 0,
                            snappedTime.minute,
                            isAmPm
                        )

                        val newTime = snappedTime.withHour(newHour)

                        if(!newTime.isBefore(minTime) && !newTime.isAfter(maxTime)) {
                            snappedTime = newTime
                        }

                        onSnappedTime(newTime)

                        return@HmTextPicker
                    }
                )
                //Minute
                HmTextPicker(
                    timeFormat = TimeFormat.MINUTE,
                    texts = minutes.map { it.text }.toImmutableList(),
                    rowCount = rowCount,
                    startIndex = minutes.find { it.value == startTime.minute }?.index?.plus(300) ?: 60,
                    onItemSelected = { snappedText ->

                        val newMinute = minutes.find { it.text == snappedText }?.value ?: 0

                        val newHour = amPmHourToHour24(
                            hours.find { it.value == localTimeToAmPmHour(snappedTime) }?.value ?: 0,
                            snappedTime.minute,
                            isAmPm
                        )

                        val newTime = snappedTime.withMinute(newMinute).withHour(newHour)

                        if(!newTime.isBefore(minTime) && !newTime.isAfter(maxTime)) {
                            snappedTime = newTime
                        }
                        onSnappedTime(snappedTime)

                        return@HmTextPicker
                    }
                )
            }
        }
    }
}

@Composable
private fun HmAmPmButton(
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
            style = JUNTheme.typography.titleLargeB,
            modifier = Modifier.padding(10.dp)
        )
    }
}

internal fun localTimeToAmPmHour(localTime: LocalTime): Int {

    if(
        isBetween(
            localTime,
            LocalTime.of(0,0),
            LocalTime.of(0,59)
        )
    ) {
        return localTime.hour + 12
    }

    if(
        isBetween(
            localTime,
            LocalTime.of(1,0),
            LocalTime.of(11,59)
        )
    ) {
        return localTime.hour
    }

    if(
        isBetween(
            localTime,
            LocalTime.of(12,0),
            LocalTime.of(12,59)
        )
    ) {
        return localTime.hour
    }

    if(
        isBetween(
            localTime,
            LocalTime.of(13,0),
            LocalTime.of(23,59)
        )
    ) {
        return localTime.hour - 12
    }

    return localTime.hour
}


private fun isBetween(localTime: LocalTime, startTime: LocalTime, endTime: LocalTime): Boolean {
    return localTime in startTime..endTime
}

private fun amPmHourToHour24(amPmHour: Int, amPmMinute: Int, amPmValue: AmPmValue): Int {

    return when(amPmValue) {
        AmPmValue.AM -> {
            if(amPmHour == 12 && amPmMinute <= 59) {
                0
            } else {
                amPmHour
            }
        }
        AmPmValue.PM -> {
            if(amPmHour == 12 && amPmMinute <= 59) {
                amPmHour
            } else {
                amPmHour + 12
            }
        }
    }
}

private data class Hour(
    val text: String,
    val value: Int,
    val index: Int
)

private data class Minute(
    val text: String,
    val value: Int,
    val index: Int
)

internal enum class AmPmValue {
    AM, PM
}

private fun amPmValueFromTime(time: LocalTime): AmPmValue {
    return if(time.hour > 11) AmPmValue.PM else AmPmValue.AM
}