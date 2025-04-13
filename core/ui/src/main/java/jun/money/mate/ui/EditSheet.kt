package jun.money.mate.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.BottomToTopAnimatedVisibility
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.theme.Gray3
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.White1

@Composable
fun EditSheet(
    selectedCount: Int,
    onEdit: () -> Unit,
    onClose: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomToTopAnimatedVisibility(
        visible = selectedCount > 0,
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 4.dp,
            color = Gray3
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 4.dp)
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "${selectedCount}개 선택",
                    style = TypoTheme.typography.titleMediumM,
                    color = White1
                )
                HorizontalSpacer(1f)
                FadeAnimatedVisibility(
                    selectedCount < 2
                ) {
                    IconButton(
                        onClick = onEdit,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = White1
                        )
                    }
                }
                IconButton(
                    onClick = onDelete,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = White1
                    )
                }
                IconButton(
                    onClick = onClose,
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = White1
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditSheetPreview() {
    JunTheme {
        EditSheet(
            selectedCount = 1,
            onEdit = {},
            onClose = {},
            onDelete = {}
        )
    }
}