package jun.money.mate.designsystem_date.datetimepicker.core.base

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import jun.money.mate.designsystem.component.DefaultBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BottomSheetBase(
    sheetState: SheetState,
    properties: DialogProperties = DialogProperties(),
    onDialogClick: (() -> Unit)? = null,
    onDismissRequest: () -> Unit,
    sheetContent: @Composable () -> Unit,
) {

    val boxInteractionSource = remember { MutableInteractionSource() }
    val contentInteractionSource = remember { MutableInteractionSource() }

    DefaultBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        sheetContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable(
                        interactionSource = boxInteractionSource,
                        indication = null,
                        onClick = {
                            if (properties.dismissOnClickOutside) onDismissRequest()
                        }
                    )
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .clickable(
                            indication = null,
                            interactionSource = contentInteractionSource,
                            onClick = { onDialogClick?.invoke() }
                        ),
                    color = Color.Transparent,
                    content = sheetContent
                )
            }
        }
    )
}

