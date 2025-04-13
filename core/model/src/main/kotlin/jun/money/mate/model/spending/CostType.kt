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
    렌트비,
}

@Serializable
enum class SubscriptionType(val category: SubscriptionCategory) {
    // 🎬 영상 스트리밍
    넷플릭스(SubscriptionCategory.스트리밍),
    유튜브(SubscriptionCategory.스트리밍),
    디즈니플러스(SubscriptionCategory.스트리밍),
    왓챠(SubscriptionCategory.스트리밍),
    웨이브(SubscriptionCategory.스트리밍),
    티빙(SubscriptionCategory.스트리밍),
    아마존프라임(SubscriptionCategory.스트리밍),
    AppleTV(SubscriptionCategory.스트리밍),

    // 🎧 음악 스트리밍
    멜론(SubscriptionCategory.음악),
    스포티파이(SubscriptionCategory.음악),
    지니뮤직(SubscriptionCategory.음악),
    벅스(SubscriptionCategory.음악),

    // 🛍️ 쇼핑 / 멤버십
    네이버플러스(SubscriptionCategory.쇼핑),
    쿠팡와우(SubscriptionCategory.쇼핑),

    // 🍽️ 배달 서비스
    배달의민족(SubscriptionCategory.배달),
    요기요(SubscriptionCategory.배달),

    // 📚 전자책 / 콘텐츠
    밀리의서재(SubscriptionCategory.전자책),
    리디셀렉트(SubscriptionCategory.전자책),

    // AI
    ChatGPT(SubscriptionCategory.AI),
    Claude(SubscriptionCategory.AI),
    GitHubCopilot(SubscriptionCategory.AI),
}

enum class SubscriptionCategory {
    스트리밍,
    음악,
    쇼핑,
    배달,
    전자책,
    AI
}
