// Theme.kt
package jun.money.mate.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    surfaceDim = Black2,
    primary = Black2,
    onPrimary = White2,
    onSurface = White2,
    onSurfaceVariant = Gray9,
    onTertiary = White2,
    surface = Black3,
    secondary = Gray3,
    tertiary = Gray3
)

private val LightColorScheme = lightColorScheme(
    surfaceDim = White,
    primary = main,
    onPrimary = main,
    secondary = Color(0xFFc7dcdc),
    tertiary = White1,
    surface = mainBackground,
    onSurface = Black, // 텍스트 색상
    onSurfaceVariant = Gray4,
    onSecondary = White,
    onTertiary = Black,
    onBackground = Color(0xFF1C1B1F),
)

val LocalDarkTheme = compositionLocalOf { true }

@Composable
fun JunTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    if (!LocalInspectionMode.current) {
        val view = LocalView.current
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalTypography provides Typography,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

@Composable
fun ChangeStatusBarColor(
    color: Color
) {
    val view = LocalView.current
    val darkTheme = LocalDarkTheme.current

    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = color.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }
}

object JUNTheme {
    val typography: JUNTypography
        @Composable
        get() = LocalTypography.current
}