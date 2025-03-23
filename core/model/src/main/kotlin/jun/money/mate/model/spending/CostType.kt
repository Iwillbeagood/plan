package jun.money.mate.model.spending

sealed interface CostType {

    data class Normal(val type: NormalType) : CostType

    data class Subscription(val type: SubscriptionType) : CostType

    data class Etc(val type: String) : CostType

    companion object {

        val CostType.name: String
            get() = when (this) {
                is Normal -> type.name
                is Subscription -> type.name
                is Etc -> type
            }
    }
}

enum class NormalType {
    교통비,
    교육,
    보험,
    통신비,
    주거비,
    관리비,
    운동,
    할부,
    렌트비,
}

enum class SubscriptionType {
    넷플릭스,
    유튜브,
    네이버플러스,
    디즈니플러스,
    왓챠,
    웨이브,
    티빙,
    쿠팡,
    아마존프라임,
    멜론,
    스포티파이,
}