package jun.money.mate.ui.interop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import jun.money.mate.model.etc.error.MessageType

val LocalMainActionInterop = staticCompositionLocalOf<MainActionInterop> {
    error("No MainActionInterop provided")
}

interface MainActionInterop {
    fun onFinish()
    fun onRestart()
    fun onShowSnackBar(messageType: MessageType)
}

@Composable
fun rememberShowSnackBar(): (MessageType) -> Unit {
    val mainActionInterop = LocalMainActionInterop.current
    return { messageType ->
        mainActionInterop.onShowSnackBar(messageType)
    }
}

@Composable
fun rememberAppFinish(): () -> Unit {
    val mainActionInterop = LocalMainActionInterop.current
    return {
        mainActionInterop.onFinish()
    }
}

@Composable
fun rememberAppRestart(): () -> Unit {
    val mainActionInterop = LocalMainActionInterop.current
    return {
        mainActionInterop.onRestart()
    }
}