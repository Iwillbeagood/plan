package jun.money.mate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.CrossfadeWithSlide
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.theme.White1
import jun.money.mate.model.etc.EditMode

@Composable
fun EditModeButton(
    editMode: EditMode,
    onAdd: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    Box(
        modifier = Modifier.background(White1),
    ) {
        CrossfadeWithSlide(
            targetState = editMode,
        ) {
            when (it) {
                EditMode.LIST -> {
                    RegularButton(
                        text = "추가",
                        onClick = onAdd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }
                EditMode.EDIT -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        RegularButton(
                            text = "삭제",
                            onClick = onDelete,
                            isActive = false,
                            modifier = Modifier.weight(1f),
                        )
                        HorizontalSpacer(10.dp)
                        RegularButton(
                            text = "수정",
                            onClick = onEdit,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                EditMode.DELETE_ONLY -> {
                    RegularButton(
                        text = "삭제",
                        onClick = onDelete,
                        isActive = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}
