package jun.money.mate.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import jun.money.mate.model.etc.ConnectionState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current
    return produceState(initialValue = context.currentConnectivityState) {
        context.observeConnectivityAsFlow().collect { value = it }
    }
}

private val Context.currentConnectivityState: ConnectionState
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

@SuppressLint("MissingPermission")
fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager,
): ConnectionState {
    val activeNetwork = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager.activeNetwork
    } else {
        return ConnectionState.Available
    }
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    val connected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

    return if (connected) ConnectionState.Available else ConnectionState.Unavailable
}

@SuppressLint("MissingPermission")
fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = NetworkCallback { connectionState -> trySend(connectionState) }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

private fun NetworkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectionState.Available)
        }

        override fun onLost(network: Network) {
            callback(ConnectionState.Unavailable)
        }
    }
}
