package jun.money.mate.main.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import jun.money.mate.designsystem.component.TwoBtnDialog
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.JUNTheme
import kic.owner2.stringres.R

@Composable
internal fun UpdateDialog(
    message: String,
    onDismissRequest: () -> Unit,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
    button1Text: String = stringResource(id = R.string.btn_no),
) {
    TwoBtnDialog(
        title = stringResource(id = kic.owner2.main.R.string.update_dialog_title),
        onDismissRequest = onDismissRequest,
        button1Click = onNegativeClick,
        button1Text = button1Text,
        button2Text = stringResource(id = R.string.btn_update),
        button2Click = onPositiveClick,
        content = {
            Text(
                text = message,
                style = JUNTheme.typography.titleNormalR,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}

@Preview
@Composable
private fun UpdateDialogPreview() {
    JunTheme {
        UpdateDialog(
            message = "업데이트 요청 메시지",
            onDismissRequest = { /*TODO*/ },
            onPositiveClick = { /*TODO*/ },
            onNegativeClick = {}
        )
    }
}