package jun.money.mate.designsystem_date.datetimepicker.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun HmTextPicker(
    modifier: Modifier = Modifier,
    timeFormat: TimeFormat = TimeFormat.HOUR,
    texts: ImmutableList<String>,
    startIndex: Int = 0,
    rowCount: Int,
    onItemSelected: (String) -> Unit,
) {
    Picker(
        modifier = modifier,
        startIndex = startIndex,
        count = texts.size,
        rowCount = rowCount,
        timeFormat = timeFormat,
        texts = texts,
        onItemSelected = onItemSelected
    )
}

enum class TimeFormat {
    HOUR, MINUTE, DAY, YEAR, MONTH
}