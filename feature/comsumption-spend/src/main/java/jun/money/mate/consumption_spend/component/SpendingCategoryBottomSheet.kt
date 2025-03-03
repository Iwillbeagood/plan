package jun.money.mate.consumption_spend.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import jun.money.mate.designsystem.component.DefaultBottomSheet
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.JunTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SpendingCategoryBottomSheet(
    consumptionPlanTitles: List<String>,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    onCategorySelected: (String) -> Unit,
) {
    DefaultBottomSheet(
        sheetState = sheetState,
        sheetTitle = "소비 계획 선택",
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
        LazyColumn {
            items(consumptionPlanTitles) { title ->
                Text(
                    text = title,
                    style = TypoTheme.typography.titleMediumR,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onCategorySelected(title)
                        }
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SpendingCategoryBottomSheetPreview() {
    JunTheme {
        SpendingCategoryBottomSheet(
            sheetState = SheetState(true, Density(1f), SheetValue.Expanded, { true }, false),
            consumptionPlanTitles = listOf(
                "식비",
                "교통비",
                "문화생활비",
            ),
            onDismiss = {},
            onCategorySelected = {}
        )
    }
}