package jun.money.mate.designsystem.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.res.R

@Composable
fun DefaultDialog(
    title: String = "",
    onDismissRequest: () -> Unit = {},
    contentSpace: Dp = 30.dp,
    contentPadding: Dp = 16.dp,
    button1: @Composable RowScope.() -> Unit = {},
    button2: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(vertical = 10.dp)
            ) {
                if (title.isNotEmpty()) {
                    Text(
                        text = title,
                        style = TypoTheme.typography.titleLargeM,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(contentSpace))
                Column(
                    modifier = Modifier
                        .padding(horizontal = contentPadding)
                ) {
                    content()
                    Spacer(modifier = Modifier.height(contentSpace))
                    Row {
                        button1()
                        button2?.let {
                            Spacer(modifier = Modifier.width(10.dp))
                            it()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TwoBtnDialog(
    title: String = "",
    onDismissRequest: () -> Unit = {},
    contentSpace: Dp = 20.dp,
    contentPadding: Dp = 16.dp,
    button1Text: String = stringResource(id = R.string.btn_no),
    button2Text: String = stringResource(id = R.string.btn_complete),
    button1Click: () -> Unit = onDismissRequest,
    button2Click: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    DefaultDialog(
        title = title,
        onDismissRequest = onDismissRequest,
        contentSpace = contentSpace,
        contentPadding = contentPadding,
        content = content,
        button1 = {
            RegularButton(
                text = button1Text,
                onClick = button1Click,
                isActive = false,
                modifier = Modifier.weight(5f),
            )
        },
        button2 = {
            RegularButton(
                text = button2Text,
                onClick = button2Click,
                modifier = Modifier.weight(5f)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(true),
    onDismissRequest: () -> Unit,
    sheetTitle: String = "",
    sheetTitleColor: Color = MaterialTheme.colorScheme.onSurface,
    sheetContentSpace: Dp = 20.dp,
    sheetButton1: (@Composable RowScope.() -> Unit)? = null,
    sheetButton2: (@Composable RowScope.() -> Unit)? = null,
    sheetContent: @Composable () -> Unit = {},
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surfaceDim,
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        properties = ModalBottomSheetProperties()
    ) {
        BackHandler(onBack = onDismissRequest)
        
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceDim)
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
                .navigationBarsPadding()
        ) {
            if (sheetTitle.isNotEmpty()) {
                Text(
                    text = sheetTitle,
                    style = TypoTheme.typography.headlineSmallB,
                    color = sheetTitleColor
                )
                Spacer(modifier = Modifier.height(sheetContentSpace))
            }
            sheetContent()
            sheetButton1?.let { button1 ->
                Spacer(modifier = Modifier.height(sheetContentSpace))
                Row {
                    button1()
                    sheetButton2?.let { button2 ->
                        Spacer(modifier = Modifier.width(10.dp))
                        button2()
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoButtonBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    sheetTitle: String = "",
    sheetTitleColor: Color = MaterialTheme.colorScheme.onSurface,
    sheetContentSpace: Dp = 20.dp,
    button1Text: String = stringResource(id = R.string.btn_no),
    button2Text: String = stringResource(id = R.string.btn_complete),
    button1Click: () -> Unit = onDismissRequest,
    button2Click: () -> Unit = {},
    sheetContent: @Composable () -> Unit = {},
) {
    DefaultBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        sheetTitle = sheetTitle,
        sheetTitleColor = sheetTitleColor,
        sheetContentSpace = sheetContentSpace,
        sheetContent = sheetContent,
        sheetButton1 = {
            RegularButton(
                text = button1Text,
                modifier = Modifier.weight(3f),
                isActive = false,
                onClick = button1Click
            )
        },
        sheetButton2 = {
            RegularButton(
                text = button2Text,
                modifier = Modifier.weight(7f),
                onClick = button2Click
            )
        },
    )
}

@Preview
@Composable
private fun TextDialogPreview() {
    JunTheme {
        DefaultDialog(
            title = "요청",
            content = {
                Text(text = "요청을 하시겠습니까?")
            },
            button1 = {
                RegularButton(
                    text = "아니요",
                    modifier = Modifier.weight(3f),
                    isActive = false,
                    onClick = {}
                )
            },
            button2 = {
                RegularButton(
                    text = "예",
                    modifier = Modifier.weight(7f),
                    onClick = {},
                )

            },
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BottomScaffoldPreview() {
    JunTheme {
        DefaultBottomSheet(
            sheetState = SheetState(true, Density(1f), SheetValue.Expanded, { true }, false),
            sheetTitle = "등록",
            sheetContent = {
                Text(
                    text = "등록을 하시겠습니까?",
                    style = TypoTheme.typography.titleLargeM,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            sheetButton1 = {
                RegularButton(
                    text = "아니요",
                    modifier = Modifier.weight(3f),
                    isActive = false,
                    onClick = {}
                )
            },
            sheetButton2 = {
                RegularButton(
                    text = "예",
                    modifier = Modifier.weight(7f),
                    onClick = {},
                )
            },
            onDismissRequest = { /*TODO*/ }
        )
    }
}