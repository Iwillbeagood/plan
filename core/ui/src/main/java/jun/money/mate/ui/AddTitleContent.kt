package jun.money.mate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JUNTheme

@Composable
fun AddTitleContent(
    title: String,
    visible: Boolean = true,
    content: @Composable () -> Unit
) {
    if (visible) {
        Column {
            VerticalSpacer(20.dp)
            Text(
                text = title,
                style = JUNTheme.typography.titleMediumM,
            )
            VerticalSpacer(10.dp)
            content()
        }
    }
}
