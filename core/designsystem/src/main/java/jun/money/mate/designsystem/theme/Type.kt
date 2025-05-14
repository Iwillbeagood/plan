package jun.money.mate.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import jun.money.mate.designsystem.R

val TextUnit.nonScaledSp
    @Composable
    @ReadOnlyComposable
    get() = (this.value / LocalDensity.current.fontScale).sp

val TextStyle.nonScaledSp
    @Composable
    @ReadOnlyComposable
    get() = this.copy(
        fontSize = this.fontSize.nonScaledSp,
        lineHeight = this.lineHeight.nonScaledSp
    )

val notosanKr = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_reqular, FontWeight.Normal)
)

private val notoanskrStyle = TextStyle(
    fontFamily = notosanKr,
    fontWeight = FontWeight.Normal,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    )
)

// Set of Material typography styles to start with
val Typography = JUNTypography(
    displayLargeB = notoanskrStyle.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold
    ),
    displayLargeM = notoanskrStyle.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Medium,
    ),
    displayLargeR = notoanskrStyle.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    displayMediumB = notoanskrStyle.copy(
        fontSize = 30.sp,
        lineHeight = 38.sp,
        fontWeight = FontWeight.Bold,
    ),
    displaySmallB = notoanskrStyle.copy(
        fontSize = 28.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.Bold,
    ),
    headlineLargeB = notoanskrStyle.copy(
        fontSize = 26.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.Bold,
    ),
    headlineMediumB = notoanskrStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Bold,
    ),
    headlineMediumM = notoanskrStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Medium,
    ),
    headlineMediumR = notoanskrStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    headlineSmallB = notoanskrStyle.copy(
        fontSize = 22.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.Bold,
    ),
    headlineSmallM = notoanskrStyle.copy(
        fontSize = 22.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.Medium,
    ),
    headlineSmallR = notoanskrStyle.copy(
        fontSize = 22.sp,
        lineHeight = 30.sp,
    ),
    titleLargeB = notoanskrStyle.copy(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleLargeM = notoanskrStyle.copy(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Medium,
    ),
    titleLargeR = notoanskrStyle.copy(
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    titleNormalB = notoanskrStyle.copy(
        fontSize = 18.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleNormalM = notoanskrStyle.copy(
        fontSize = 18.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Medium,
    ),
    titleNormalR = notoanskrStyle.copy(
        fontSize = 18.sp,
        lineHeight = 26.sp,
    ),
    titleMediumB = notoanskrStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleMediumM = notoanskrStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium,
    ),
    titleMediumR = notoanskrStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    titleSmallB = notoanskrStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.25.sp,
    ),
    titleSmallM = notoanskrStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.25.sp,
    ),
    titleSmallR = notoanskrStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelLargeB = notoanskrStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Bold,
    ),
    labelLargeM = notoanskrStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
    ),
    labelLargeR = notoanskrStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelMediumB = notoanskrStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 14.sp,
    ),
    labelMediumM = notoanskrStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 14.sp,
    ),
    labelMediumR = notoanskrStyle.copy(
        fontSize = 10.sp,
        lineHeight = 14.sp,
    ),
    labelSmallM = notoanskrStyle.copy(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = (-0.2).sp,
    ),
    bodyLargeR = notoanskrStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMediumR = notoanskrStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmallR = notoanskrStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )
)
@Immutable
data class JUNTypography(
    val displayLargeB: TextStyle,
    val displayLargeM: TextStyle,
    val displayLargeR: TextStyle,
    val displayMediumB: TextStyle,
    val displaySmallB: TextStyle,

    val headlineLargeB: TextStyle,
    val headlineMediumB: TextStyle,
    val headlineMediumM: TextStyle,
    val headlineMediumR: TextStyle,
    val headlineSmallB: TextStyle,
    val headlineSmallM: TextStyle,
    val headlineSmallR: TextStyle,

    val titleLargeB: TextStyle,
    val titleLargeM: TextStyle,
    val titleLargeR: TextStyle,
    val titleNormalB: TextStyle,
    val titleNormalM: TextStyle,
    val titleNormalR: TextStyle,
    val titleMediumB: TextStyle,
    val titleMediumM: TextStyle,
    val titleMediumR: TextStyle,
    val titleSmallB: TextStyle,
    val titleSmallM: TextStyle,
    val titleSmallR: TextStyle,

    val labelLargeB: TextStyle,
    val labelLargeM: TextStyle,
    val labelLargeR: TextStyle,
    val labelMediumB: TextStyle,
    val labelMediumM: TextStyle,
    val labelMediumR: TextStyle,
    val labelSmallM: TextStyle,

    val bodyLargeR: TextStyle,
    val bodyMediumR: TextStyle,
    val bodySmallR: TextStyle,
)

val LocalTypography = staticCompositionLocalOf {
    JUNTypography(
        displayLargeB = notoanskrStyle,
        displayLargeM = notoanskrStyle,
        displayLargeR = notoanskrStyle,
        displayMediumB = notoanskrStyle,
        displaySmallB = notoanskrStyle,
        headlineLargeB = notoanskrStyle,
        headlineMediumB = notoanskrStyle,
        headlineMediumM = notoanskrStyle,
        headlineMediumR = notoanskrStyle,
        headlineSmallB = notoanskrStyle,
        headlineSmallM = notoanskrStyle,
        headlineSmallR = notoanskrStyle,
        titleLargeB = notoanskrStyle,
        titleLargeM = notoanskrStyle,
        titleLargeR = notoanskrStyle,
        titleNormalB = notoanskrStyle,
        titleNormalM = notoanskrStyle,
        titleNormalR = notoanskrStyle,
        titleMediumB = notoanskrStyle,
        titleMediumM = notoanskrStyle,
        titleMediumR = notoanskrStyle,
        titleSmallB = notoanskrStyle,
        titleSmallM = notoanskrStyle,
        titleSmallR = notoanskrStyle,
        labelLargeB = notoanskrStyle,
        labelLargeM = notoanskrStyle,
        labelLargeR = notoanskrStyle,
        labelMediumB = notoanskrStyle,
        labelMediumM = notoanskrStyle,
        labelMediumR = notoanskrStyle,
        labelSmallM = notoanskrStyle,
        bodyLargeR = notoanskrStyle,
        bodyMediumR = notoanskrStyle,
        bodySmallR = notoanskrStyle,
    )
}
