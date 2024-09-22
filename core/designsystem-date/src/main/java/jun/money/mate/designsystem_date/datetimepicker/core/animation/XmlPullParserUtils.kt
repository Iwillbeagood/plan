package jun.money.mate.designsystem_date.datetimepicker.core.animation

import android.content.res.Resources
import android.content.res.TypedArray
import android.util.AttributeSet
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

internal fun XmlPullParser.isAtEnd(): Boolean =
    eventType == XmlPullParser.END_DOCUMENT ||
        (depth < 1 && eventType == XmlPullParser.END_TAG)

/**
 * Helper method to seek to the first tag within the VectorDrawable xml asset
 */
@Throws(XmlPullParserException::class)
internal fun XmlPullParser.seekToStartTag(): XmlPullParser {
    var type = next()
    while (type != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
        // Empty loop
        type = next()
    }
    if (type != XmlPullParser.START_TAG) {
        throw XmlPullParserException("No start tag found")
    }
    return this
}

/**
 * Assuming that we are at the [XmlPullParser.START_TAG start] of the specified [tag], iterates
 * though the events until we see a corresponding [XmlPullParser.END_TAG].
 */
internal inline fun XmlPullParser.forEachChildOf(
    tag: String,
    action: XmlPullParser.() -> Unit
) {
    next()
    while (!isAtEnd()) {
        if (eventType == XmlPullParser.END_TAG && name == tag) {
            break
        }
        action()
        next()
    }
}

internal inline fun <T> AttributeSet.attrs(
    res: Resources,
    theme: Resources.Theme?,
    styleable: IntArray,
    body: (a: TypedArray) -> T
): T {
    val a = theme?.obtainStyledAttributes(this, styleable, 0, 0)
        ?: res.obtainAttributes(this, styleable)
    try {
        return body(a)
    } finally {
        a.recycle()
    }
}
