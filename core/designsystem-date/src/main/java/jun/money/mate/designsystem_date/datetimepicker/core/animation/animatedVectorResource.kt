package jun.money.mate.designsystem_date.datetimepicker.core.animation

import android.content.res.Resources
import android.util.Xml
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kic.owner2.designsystem_date.datetimepicker.core.util.ExperimentalAnimationGraphicsApi
import org.xmlpull.v1.XmlPullParserException

/**
 * Load an [AnimatedImageVector] from an Android resource id.
 *
 * @param id the resource identifier
 * @return an animated vector drawable resource.
 *
 * @sample androidx.compose.animation.graphics.samples.AnimatedVectorSample
 */
@ExperimentalAnimationGraphicsApi
@Composable
fun AnimatedImageVector.Companion.animatedVectorResource(
    @DrawableRes id: Int
): AnimatedImageVector {
    val context = LocalContext.current
    val res = context.resources
    val theme = context.theme
    return remember(id) {
        loadAnimatedVectorResource(theme, res, id)
    }
}

@ExperimentalAnimationGraphicsApi
@Throws(XmlPullParserException::class)
internal fun loadAnimatedVectorResource(
    theme: Resources.Theme? = null,
    res: Resources,
    resId: Int
): AnimatedImageVector {
    val parser = res.getXml(resId).seekToStartTag()
    val attrs = Xml.asAttributeSet(parser)
    return parser.parseAnimatedImageVector(res, theme, attrs)
}
