package jun.money.mate.cost

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.cost.component.CostCalendar
import jun.money.mate.cost.component.CostEditSheet
import jun.money.mate.cost.component.CostList
import jun.money.mate.cost.contract.CostEffect
import jun.money.mate.cost.contract.CostModalEffect
import jun.money.mate.cost.contract.CostState
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TwoBtnDialog
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.spending.Cost
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar

@Composable
internal fun CostRoute(
    viewModel: CostViewModel = hiltViewModel()
) {
    val uiState by viewModel.costState.collectAsStateWithLifecycle()
    val modalEffect by viewModel.costModalEffect.collectAsStateWithLifecycle()
    val navigateAction = LocalNavigateActionInterop.current
    val showSnackBar = rememberShowSnackBar()

    CostContent(
        uiState = uiState,
        viewModel = viewModel,
        onShowCostAddScreen = navigateAction::navigateToCostAdd
    )

    CostModalContent(
        modalEffect = modalEffect,
        viewModel = viewModel
    )

    LaunchedEffect(Unit) {
        viewModel.costEffect.collect {
            when (it) {
                is CostEffect.ShowSnackBar -> showSnackBar(it.messageType)
                is CostEffect.NavigateToCostDetail -> navigateAction.navigateToCostDetail(it.id)
            }
        }
    }
}

@Composable
private fun CostContent(
    uiState: CostState,
    viewModel: CostViewModel,
    onShowCostAddScreen: () -> Unit,
) {
    FadeAnimatedVisibility(
        visible = uiState is CostState.Data
    ) {
        if (uiState is CostState.Data) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CostScreen(
                    uiState = uiState,
                    onSelectCost = viewModel::selectCost,
                    onShowCostAddScreen = onShowCostAddScreen
                )
                CostEditSheet(
                    selectedCount = uiState.selectedCount,
                    onEdit = viewModel::editCost,
                    onClose = viewModel::unselectCost,
                    onDelete = viewModel::showDeleteDialog,
                    modifier = Modifier
                        .padding(vertical = 30.dp, horizontal = 16.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun CostScreen(
    uiState: CostState.Data,
    onSelectCost: (Cost) -> Unit,
    onShowCostAddScreen: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VerticalSpacer(50.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(start = 26.dp)
            ) {
                Text(
                    text = "이번달의 전체 소비",
                    style = TypoTheme.typography.titleLargeM,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                VerticalSpacer(4.dp)
                Text(
                    text = uiState.totalCostString,
                    style = TypoTheme.typography.displayLargeB,
                )
            }
            HorizontalSpacer(1f)
            RegularButton(
                text = "소비 추가",
                onClick = onShowCostAddScreen,
                style = TypoTheme.typography.titleLargeB,
                verticalPadding = 20.dp,
                horizontalPadding = 20.dp,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            )
            HorizontalSpacer(24.dp)
        }
        VerticalSpacer(50.dp)
        HorizontalDivider(10.dp)
        if (uiState.costs.isNotEmpty()) {
            VerticalSpacer(20.dp)
            CostCalendar(
                costCalendarValue = uiState.costCalendarValues
            )
            VerticalSpacer(20.dp)
            Text(
                text = "소비 내역",
                style = TypoTheme.typography.titleNormalM,
                modifier = Modifier.padding(start = 26.dp)
            )
            VerticalSpacer(10.dp)
            CostList(
                costs = uiState.costs,
                onCostClick = onSelectCost
            )
        }
    }
}

@Composable
private fun CostModalContent(
    modalEffect: CostModalEffect,
    viewModel: CostViewModel
) {
    when (modalEffect) {
        CostModalEffect.Hidden -> {}
        CostModalEffect.ShowDeleteDialog -> {
            TwoBtnDialog(
                onDismissRequest = viewModel::hideModal,
                button2Text = stringResource(id = jun.money.mate.res.R.string.btn_yes),
                button2Click = viewModel::deleteCost,
                content = {
                    Text(
                        text = "선택한 소비를 삭제하시겠습니까?",
                        style = TypoTheme.typography.titleMediumM
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CostScreenPreview() {
    JunTheme {
        CostScreen(
            uiState = CostState.Data(
                costs = Cost.samples
            ),
            onSelectCost = {},
            onShowCostAddScreen = {}
        )
    }
}