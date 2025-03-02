package jun.money.mate.income.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jun.money.mate.designsystem.component.TextButton
import jun.money.mate.designsystem.theme.Gray6
import jun.money.mate.designsystem.theme.White1

@Composable
internal fun TypeButton(
    text: String,
    isType: Boolean,
    onApplyType: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        text = text,
        onClick = onApplyType,
        color = if (isType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceDim,
        textColor = if (isType) White1 else MaterialTheme.colorScheme.onSurface,
        borderColor = if (isType) MaterialTheme.colorScheme.primary else Gray6,
        modifier = modifier
    )
}