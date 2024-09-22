package jun.money.mate.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * compose 네비게이션 인자를 Parcelable로 변환해서 전달하기 위한 타입
 */
inline fun <reified T : Parcelable> navTypeOf(): NavType<T> {
    return object : NavType<T>(false) {
        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putParcelable(key, value)
        }

        override fun get(bundle: Bundle, key: String): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // API 33 이상
                bundle.getParcelable(key, T::class.java)
            } else {
                // API 33 미만
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