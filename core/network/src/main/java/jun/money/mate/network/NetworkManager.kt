package jun.money.mate.network

import android.content.Context
import jun.money.mate.model.etc.ConnectionState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NetworkManager @Inject constructor(
    private val context: Context,
) {

    fun connectivityFlow(): Flow<ConnectionState> {
        return context.observeConnectivityAsFlow()
    }
}
