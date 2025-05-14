package jun.money.mate.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.BottomToTopSlideFadeAnimatedVisibility
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TopAppbar

@Composable
fun AddScaffold(
    color: Color,
    onGoBack: () -> Unit,
    onComplete: () -> Unit,
    title: String = "",
    buttonVisible: Boolean = true,
    content: @Composable () -> Unit,
) {
    AddScaffold(
        buttonText = "추가하기",
        onGoBack = onGoBack,
        onComplete = onComplete,
        buttonVisible = buttonVisible,
        content = content,
    )
}

@Composable
fun AddScaffold(
    title: String = "",
    buttonText: String,
    onGoBack: () -> Unit,
    onComplete: () -> Unit,
    buttonVisible: Boolean = true,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppbar(
                title = title,
                onBackEvent = onGoBack,
            )
        },
        bottomBar = {
            BottomToTopSlideFadeAnimatedVisibility(
                visible = buttonVisible,
                modifier = Modifier,
            ) {
                RegularButton(
                    text = buttonText,
                    onClick = onComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .padding(horizontal = 10.dp),
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier
            .imePadding(),
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .padding(horizontal = 16.dp),
        ) {
            content()
        }
    }
}
