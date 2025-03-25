package jun.money.mate.model.spending

import kotlinx.serialization.Serializable

@Serializable
sealed interface CostType {

    @Serializable
    data class Normal(
        val normalType: NormalType
    ) : CostType

    @Serializable
    data class Subscription(
        val subscriptionType: SubscriptionType
    ) : CostType

    @Serializable
    data class Etc(val type: String) : CostType

    companion object {

        val CostType.name: String
            get() = when (this) {
                is Normal -> normalType.name
                is Subscription -> subscriptionType.name
                is Etc -> type
            }
    }
}

@Serializable
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

@Serializable
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