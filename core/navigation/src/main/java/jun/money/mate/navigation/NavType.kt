package jun.money.mate.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

inline fun <reified T : Parcelable> navTypeOf(): NavType<T> {
    return object : NavType<T>(false) {
        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putParcelable(key, value)
        }

        override fun get(bundle: Bundle, key: String): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, T::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(key)
            }
        }

        override fun parseValue(value: String): T {
            return Json.decodeFromString(serializer(), value)
        }

        override fun serializeAsValue(value: T): String {
            return Json.encodeToString(serializer(), value)
        }
    }
}