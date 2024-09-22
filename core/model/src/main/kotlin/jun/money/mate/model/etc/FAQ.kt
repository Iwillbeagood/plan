package jun.money.mate.model.etc

data class FAQ(
    val question: String,
    val answer: String,
    val isExpanded: Boolean = false
)
