
package jun.money.mate.save.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.DefaultBottomSheet
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.theme.JUNTheme
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.model.save.SaveCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SaveCategoryBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    onCategorySelected: (SaveCategory) -> Unit
) {
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(4)
        ) {
            items(SaveCategory.entries) { category ->
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
    category: SaveCategory,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = category.name,
            style = JUNTheme.typography.titleSmallR,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SaveCategoryBottomSheetPreview() {
    JunTheme {
        SaveCategoryBottomSheet(
            sheetState = SheetState(true, Density(1f), SheetValue.Expanded, { true }, false),
            onDismiss = {},
            onCategorySelected = {}
        )
    }
}