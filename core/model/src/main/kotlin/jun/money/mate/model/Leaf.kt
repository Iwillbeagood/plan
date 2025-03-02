package jun.money.mate.model

data class Leaf(
    val startX: Float,
    val startY: Float,
    val endY: Float,
    val swingAmount: Float,
    val isRed: Boolean = false
)


data class LeafOrder(
    val isRed: Boolean
)