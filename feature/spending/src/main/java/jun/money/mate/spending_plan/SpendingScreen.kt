package jun.money.mate.spending_plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.spending.Cost
import jun.money.mate.spending_plan.component.CostList


// 상단에는 소비 아이템들이 보이면 될거 같은데,,
// 리스트 디자인과
@Composable
internal fun SpendingRoute(
    viewModel: SpendingViewModel = hiltViewModel()
) {
    val uiState by viewModel.spendingState.collectAsStateWithLifecycle()

    SpendingContent(
        uiState = uiState,
        viewModel = viewModel
    )
}

@Composable
private fun SpendingContent(
    uiState: SpendingState,
    viewModel: SpendingViewModel
) {
    FadeAnimatedVisibility(
        visible = uiState is SpendingState.Data
    ) {
        if (uiState is SpendingState.Data) {
            SpendingScreen(uiState)
        }
    }
}

// 전체 소비
@Composable
private fun SpendingScreen(
    uiState: SpendingState.Data
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VerticalSpacer(40.dp)
        Text(
            text = "이번달의 전체 소비",
            style = TypoTheme.typography.titleLargeM,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 26.dp)
        )
        VerticalSpacer(4.dp)
        Text(
            text = uiState.totalCostString,
            style = TypoTheme.typography.displayLargeB,
            modifier = Modifier.padding(start = 26.dp)
        )
        VerticalSpacer(40.dp)
        HorizontalDivider(10.dp)
        VerticalSpacer(20.dp)
        Text(
            text = "소비",
            style = TypoTheme.typography.titleNormalM,
            modifier = Modifier.padding(start = 26.dp)
        )
        VerticalSpacer(10.dp)
        CostList(
            costs = uiState.costs
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingScreenPreview() {
    JunTheme {
        SpendingScreen(
            uiState = SpendingState.Data(
                costs = Cost.samples
            )
        )
    }
}