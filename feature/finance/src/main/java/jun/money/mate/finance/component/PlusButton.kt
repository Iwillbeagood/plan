package jun.money.mate.finance.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1

@Composable
internal fun PlusButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primary,
        onClick = onClick,
        shadowElevation = 2.dp,
        modifier = modifier.padding(16.dp),
    ) {
        Text(
            text = "챌린지 시작하기",
            style = TypoTheme.typography.titleMediumB,
            color = White1,
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(16.dp)
        )
    }
}
