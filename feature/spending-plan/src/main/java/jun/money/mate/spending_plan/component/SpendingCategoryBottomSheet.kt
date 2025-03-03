
package jun.money.mate.spending_plan.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.DefaultBottomSheet
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.ScrollableTab
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.model.spending.SpendingCategoryType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SpendingCategoryBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    onCategorySelected: (SpendingCategoryType) -> Unit
) {
    var spendingTypeTabIndex by remember { mutableIntStateOf(0) }

    DefaultBottomSheet(
        sheetState = sheetState,
        sheetTitle = "카테고리 선택",
        onDismissRequest = onDismiss,
        sheetButton1 = {
            RegularButton(
                text = "취소",
                modifier = Modifier.fillMaxWidth(),
                isActive = false,
                onClick = onDismiss
            )
        }
    ) {
        ScrollableTab(
            tabs = listOf("구독", "일반"),
            selectedTabIndex = spendingTypeTabIndex,
            onTabClick = {
                spendingTypeTabIndex = it
            },
            modifier = Modifier.padding(bottom = 20.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4)
        ) {
            items(SpendingCategoryType.values(isSubscribe = spendingTypeTabIndex == 0)) { category ->
                CategoryItem(
                    category = category,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10))
                        .clickable {
                            onCategorySelected(category)
                        }
                        .padding(vertical = 10.dp)
                    ,
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier,
    category: SpendingCategoryType,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        CategoryIcon(category)
        VerticalSpacer(3.dp)
        Text(
            text = category.name,
            style = TypoTheme.typography.titleSmallR,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SpendingCategoryBottomSheetPreview() {
    JunTheme {
        SpendingCategoryBottomSheet(
            sheetState = SheetState(true, Density(1f), SheetValue.Expanded, { true }, false),
            onDismiss = {},
            onCategorySelected = {}
        )
    }
}