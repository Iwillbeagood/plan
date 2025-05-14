package jun.money.mate.utils

import jun.money.mate.model.spending.CostType
import jun.money.mate.model.spending.NormalType
import jun.money.mate.model.spending.SubscriptionType
import jun.money.mate.res.R

fun CostType.toImageRes(): Int = when (this) {
    is CostType.Subscription -> this.subscriptionType.toImageRes()
    is CostType.Normal -> this.normalType.toImageRes()
    is CostType.Etc -> R.drawable.ic_coin
}

fun SubscriptionType.toImageRes(): Int = when (this) {
    SubscriptionType.넷플릭스 -> R.drawable.ic_netflix
    SubscriptionType.유튜브 -> R.drawable.ic_youtube
    SubscriptionType.네이버플러스 -> R.drawable.ic_naver
    SubscriptionType.디즈니플러스 -> R.drawable.ic_disney_plus
    SubscriptionType.왓챠 -> R.drawable.ic_watcha
    SubscriptionType.웨이브 -> R.drawable.ic_wavve
    SubscriptionType.티빙 -> R.drawable.ic_tving
    SubscriptionType.쿠팡와우 -> R.drawable.ic_coupang
    SubscriptionType.아마존프라임 -> R.drawable.ic_amazon_prime
    SubscriptionType.멜론 -> R.drawable.ic_melon
    SubscriptionType.스포티파이 -> R.drawable.ic_spotify
    SubscriptionType.AppleTV -> R.drawable.ic_apply_tv
    SubscriptionType.지니뮤직 -> R.drawable.ic_genie
    SubscriptionType.벅스 -> R.drawable.ic_bugs
    SubscriptionType.배달의민족 -> R.drawable.ic_woowa
    SubscriptionType.요기요 -> R.drawable.ic_yogiyo
    SubscriptionType.밀리의서재 -> R.drawable.ic_millie
    SubscriptionType.리디셀렉트 -> R.drawable.ic_ridi
    SubscriptionType.ChatGPT -> R.drawable.ic_gpt
    SubscriptionType.Claude -> R.drawable.ic_claude
    SubscriptionType.GitHubCopilot -> R.drawable.ic_copilot
}

fun NormalType.toImageRes(): Int = when (this) {
    NormalType.교통비 -> R.drawable.ic_tranportation
    NormalType.교육 -> R.drawable.ic_education
    NormalType.보험 -> R.drawable.ic_insurance
    NormalType.통신비 -> R.drawable.ic_phone
    NormalType.주거비 -> R.drawable.ic_home
    NormalType.관리비 -> R.drawable.ic_earnings
    NormalType.운동 -> R.drawable.ic_gym
    NormalType.렌트비 -> R.drawable.ic_earning
}
