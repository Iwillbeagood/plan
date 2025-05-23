package jun.money.mate.cost

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.cost.component.CostCalendar
import jun.money.mate.cost.component.CostCalendarValue
import jun.money.mate.cost.component.CostItem
import jun.money.mate.cost.contract.CostEffect
import jun.money.mate.cost.contract.CostModalEffect
import jun.money.mate.cost.contract.CostState
import jun.money.mate.cost.navigation.Title
import jun.money.mate.designsystem.component.HorizontalDivider
import jun.money.mate.designsystem.component.HorizontalSpacer
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TwoBtnDialog
import jun.money.mate.designsystem.component.StateAnimatedVisibility
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.Gray9
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.model.spending.Cost
import jun.money.mate.model.spending.CostType
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.navigation.interop.rememberShowSnackBar
import jun.money.mate.res.R
import jun.money.mate.ui.EditSheet
import jun.money.mate.utils.toImageRes

@Composable
internal fun CostRoute(
    viewModel: CostViewModel = hiltViewModel()
) {
    ChangeStatusBarColor()

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
    StateAnimatedVisibility<CostState.Data>(
        target = uiState,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CostScreen(
                uiState = it,
                onSelectCost = viewModel::selectCost,
                onShowCostAddScreen = onShowCostAddScreen,
                onSelectedCalendarValue = viewModel::selectCalendarValue
            )
            EditSheet(
                selectedCount = it.selectedCount,
                onEdit = viewModel::editCost,
                onClose = viewModel::unselectCost,
                onDelete = viewModel::showDeleteDialog,
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun CostScreen(
    uiState: CostState.Data,
    onSelectCost: (Cost) -> Unit,
    onShowCostAddScreen: () -> Unit,
    onSelectedCalendarValue: (CostCalendarValue?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        VerticalSpacer(50.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Text(
                    text = "이번달의 전체 $Title",
                    style = TypoTheme.typography.titleSmallM,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                VerticalSpacer(4.dp)
                Row {
                    Text(
                        text = uiState.totalCostString,
                        style = TypoTheme.typography.displayLargeB,
                    )
                    Text(
                        text = "원",
                        style = TypoTheme.typography.displayLargeM,
                    )
                }
            }
        }
        VerticalSpacer(16.dp)
        RegularButton(
            text = "$Title 추가",
            onClick = onShowCostAddScreen,
            style = TypoTheme.typography.titleNormalB,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            borderStroke = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        VerticalSpacer(10.dp)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            item {
                Column {
                    VerticalSpacer(10.dp)
                    CostCalendar(
                        costCalendarValue = uiState.costCalendarValues,
                        selectedCalendarValue = uiState.selectedCalendarValue,
                        onSelectedCalendarValue = onSelectedCalendarValue,
                    )
                    VerticalSpacer(30.dp)
                    if (uiState.costs.isNotEmpty()) {
                        Crossfade(
                            uiState.selectedCalendarValue
                        ) {
                            Text(
                                text = if (it != null)
                                    "${it.date}일 $Title"
                                else
                                    "전체 $Title",
                                style = TypoTheme.typography.titleMediumM,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                        VerticalSpacer(4.dp)
                    }
                }
            }

            items(uiState.costsByCalendar) { cost ->
                CostItem(
                    cost = cost,
                    imageRes = cost.costType.toImageRes(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            onSelectCost(cost)
                        }
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                VerticalSpacer(100.dp)
            }
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
                button2Text = stringResource(id = R.string.btn_yes),
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
            onShowCostAddScreen = {},
            onSelectedCalendarValue = {}
        )
    }
}