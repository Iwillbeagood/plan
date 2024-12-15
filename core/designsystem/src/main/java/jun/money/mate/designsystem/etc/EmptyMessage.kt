package jun.money.mate.designsystem.etc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import jun.money.mate.designsystem.theme.JUNTheme

@Composable
fun EmptyMessage(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        Text(
            text = text,
            style = JUNTheme.typography.titleMediumR,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}