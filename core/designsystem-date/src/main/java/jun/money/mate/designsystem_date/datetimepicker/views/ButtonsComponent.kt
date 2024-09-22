package jun.money.mate.designsystem_date.datetimepicker.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.RegularButton

@Composable
internal fun ButtonsComponent(
    onDisMissRequest: () -> Unit,
    onPositive: () -> Unit,
    onNegative: () -> Unit,
    onPositiveValid: Boolean = true,
) {
    Row(
        modifier = Modifier.padding(top = 20.dp)
    ) {
        RegularButton(
            text = "취소",
            modifier = Modifier.weight(3f),
            isActive = false,
            onClick = {
                onNegative()
                onDisMissRequest()
            }
        )
        Spacer(modifier = Modifier.width(10.dp))
        RegularButton(
            text = "선택하기",
            isActive = onPositiveValid,
            modifier = Modifier.weight(7f),
            onClick = {
                if (onPositiveValid) {
                    onPositive()
                    onDisMissRequest()
                }
            }
        )
    }
}

