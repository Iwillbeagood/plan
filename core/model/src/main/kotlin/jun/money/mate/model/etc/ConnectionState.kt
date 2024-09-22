package jun.money.mate.model.etc

sealed interface ConnectionState {
    data object Available : ConnectionState
    data object Unavailable : ConnectionState
}