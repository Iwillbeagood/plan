package jun.money.mate.spending_plan.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.model.spending.SpendingCategoryType
import jun.money.mate.res.R

@Composable
internal fun CategoryIcon(
    category: SpendingCategoryType,
) {
    Icon(
        painter = painterResource(id = categoryIcon(category)),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier
            .size(30.dp)
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
        SpendingCategoryType.구독료 -> R.drawable.ic_subscribe
        SpendingCategoryType.운동 -> R.drawable.ic_gym
        SpendingCategoryType.할부 -> R.drawable.ic_bill
        SpendingCategoryType.렌트비 -> R.drawable.ic_rant
        SpendingCategoryType.기타 -> R.drawable.ic_etc
    }
}

@Preview
@Composable
private fun CategoryIconPreview() {
    JunTheme {
        CategoryIcon(SpendingCategoryType.교통비)
    }
}