package jun.money.mate.model.etc.error

sealed class MessageType {
    data class Message(val message: String) : MessageType()
    data class ResId(val resId: Int) : MessageType()
}
