package jun.money.mate.spending_plan.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.model.spending.SpendingCategoryType
import jun.money.mate.res.R

@Composable
internal fun CategoryIcon(
    category: SpendingCategoryType,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    size: Dp = 30.dp
) {
    Icon(
        painter = painterResource(id = categoryIcon(category)),
        contentDescription = null,
        tint = color,
        modifier = modifier.size(size)
    )
}

private fun categoryIcon(spendingCategoryType: SpendingCategoryType): Int {
    return when (spendingCategoryType) {
        SpendingCategoryType.교통비 -> R.drawable.ic_train
        SpendingCategoryType.주거비 -> R.drawable.ic_home
        SpendingCategoryType.교육 -> R.drawable.ic_house
        SpendingCategoryType.보험 -> R.drawable.ic_health
        SpendingCategoryType.통신비 -> R.drawable.ic_phone
        SpendingCategoryType.관리비 -> R.drawable.ic_manage
        SpendingCategoryType.운동 -> R.drawable.ic_gym
        SpendingCategoryType.할부 -> R.drawable.ic_bill
        SpendingCategoryType.렌트비 -> R.drawable.ic_rant
        SpendingCategoryType.기타 -> R.drawable.ic_etc
        SpendingCategoryType.넷플릭스 -> R.drawable.ic_netflix
        SpendingCategoryType.유튜브 -> R.drawable.ic_youtube
        SpendingCategoryType.디즈니플러스 -> R.drawable.ic_disney_plus
        SpendingCategoryType.아마존프라임 -> R.drawable.ic_amazon_prime
        SpendingCategoryType.왓챠 -> R.drawable.ic_watcha
        SpendingCategoryType.웨이브 -> R.drawable.ic_wavve
        SpendingCategoryType.티빙 -> R.drawable.ic_tving
        SpendingCategoryType.쿠팡 -> R.drawable.ic_coupang
        SpendingCategoryType.멜론 -> R.drawable.ic_melon
        SpendingCategoryType.스포티파이 -> R.drawable.ic_spotify
        SpendingCategoryType.네이버플러스 -> R.drawable.ic_naver
    }
}

@Preview
@Composable
private fun CategoryIconPreview() {
    JunTheme {
        CategoryIcon(SpendingCategoryType.교통비)
    }
}