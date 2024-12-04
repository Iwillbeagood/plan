package jun.money.mate.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.BottomToTopSlideFadeAnimatedVisibility
import jun.money.mate.designsystem.component.CircleButton
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.TopAppbarType
import jun.money.mate.designsystem.component.IconButton
import jun.money.mate.designsystem.component.TopAppbar

@Composable
fun DefaultScaffold(
    color: Color,
    bottomBarVisible: Boolean,
    addButtonVisible: Boolean,
    onAdd: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        bottomBar = {
            BottomToTopSlideFadeAnimatedVisibility(
                visible = bottomBarVisible,
            ) {
                Surface(
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    color = MaterialTheme.colorScheme.surfaceDim,
                    shadowElevation = 20.dp,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            icon = Icons.Default.EditNote,
                            text = "수정",
                            modifier = Modifier
                                .weight(1f)
                                .clickable(onClick = onEdit)
                                .padding(10.dp)
                        )
                        IconButton(
                            icon = Icons.Default.DeleteOutline,
                            text = "삭제",
                            modifier = Modifier
                                .weight(1f)
                                .clickable(onClick = onDelete)
                                .padding(5.dp)
                        )
                    }
                }

            }
        },
        floatingActionButton = {
            FadeAnimatedVisibility(
                visible = addButtonVisible,
            ) {
                CircleButton(
                    size = 56.dp,
                    icon = Icons.Default.Add,
                    elevation = 8.dp,
                    color = color,
                    onClick = onAdd
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            content()
        }
    }
}