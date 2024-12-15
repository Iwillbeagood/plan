
package jun.money.mate.income.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import jun.money.mate.model.income.IncomeType
import jun.money.mate.model.save.SaveCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IncomeTypeBottomSheet(
    onDismiss: () -> Unit,
    onTypeSelected: (IncomeType) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(true),
) {
    DefaultBottomSheet(
        sheetState = sheetState,
        sheetTitle = "수입 유형을 선택해 주세요",
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
        LazyColumn{
            items(IncomeType.entries) { category ->
                CategoryItem(
                    title = category.title,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10))
                        .clickable {
                            onTypeSelected(category)
                        }
                        .padding(vertical = 20.dp)
                    ,
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier,
    title: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = JUNTheme.typography.titleMediumM,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SaveCategoryBottomSheetPreview() {
    JunTheme {
        IncomeTypeBottomSheet(
            sheetState = SheetState(true, Density(1f), SheetValue.Expanded, { true }, false),
            onDismiss = {},
            onTypeSelected = {}
        )
    }
}