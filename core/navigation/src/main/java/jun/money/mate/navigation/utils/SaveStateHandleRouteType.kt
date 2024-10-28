package jun.money.mate.navigation.utils

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute

inline fun <reified T : Any, reified Type : Any> SavedStateHandle.toRouteType(
): T = toRoute<T>(
    typeMap = createTypeMap(Type::class)
)