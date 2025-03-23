package jun.money.mate.utils

import jun.money.mate.model.spending.SubscriptionType
import jun.money.mate.res.R

fun SubscriptionType.toImageRes(): Int = when (this) {
    SubscriptionType.넷플릭스 -> R.drawable.ic_netflix
    SubscriptionType.유튜브 -> R.drawable.ic_youtube
    SubscriptionType.네이버플러스 -> R.drawable.ic_naver
    SubscriptionType.디즈니플러스 -> R.drawable.ic_disney_plus
    SubscriptionType.왓챠 -> R.drawable.ic_watcha
    SubscriptionType.웨이브 -> R.drawable.ic_wavve
    SubscriptionType.티빙 -> R.drawable.ic_tving
    SubscriptionType.쿠팡 -> R.drawable.ic_coupang
    SubscriptionType.아마존프라임 -> R.drawable.ic_amazon_prime
    SubscriptionType.멜론 -> R.drawable.ic_melon
    SubscriptionType.스포티파이 -> R.drawable.ic_spotify
}