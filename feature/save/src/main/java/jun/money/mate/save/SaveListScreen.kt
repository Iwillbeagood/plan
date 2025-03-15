package jun.money.mate.save

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.R
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TopAppbarIcon
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.main10
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.save.component.AcornBox
import jun.money.mate.save.component.SaveListBody
import jun.money.mate.save.contract.SavingListEffect
import jun.money.mate.save.contract.SavingListState
import jun.money.mate.ui.interop.LocalNavigateActionInterop
import jun.money.mate.ui.interop.rememberShowSnackBar
import jun.money.mate.utils.formatDateBasedOnYear
import java.time.YearMonth

@Composable
internal fun SaveListRoute(
    viewModel: SavingListViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(main10)

    val navigateAction = LocalNavigateActionInterop.current
    val showSnackBar = rememberShowSnackBar()
    val savingListState by viewModel.savingListState.collectAsStateWithLifecycle()

    SavingListScreen(
        savingListState = savingListState,
        month = viewModel.month,
        onShowDetail = navigateAction::navigateToSavingDetail,
        onSavingAdd = navigateAction::navigateToSavingAdd,
        onExecuteChange = viewModel::executeChange,
        onGoBack = navigateAction::popBackStack,
    )

    LaunchedEffect(Unit) {
        viewModel.savingListEffect.collect { effect ->
            when (effect) {
                is SavingListEffect.EditSpendingPlan -> navigateAction.navigateToSavingDetail(effect.id)
                is SavingListEffect.ShowSnackBar -> showSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun SavingListScreen(
    savingListState: SavingListState,
    month: YearMonth,
    onShowDetail: (Long) -> Unit,
    onSavingAdd: () -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
    onGoBack: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            RegularButton(
                text = "추가",
                onClick = onSavingAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceDim)
                    .padding(16.dp)
            )
        },
        containerColor = main10
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                AcornBox(
                    count = (savingListState as? SavingListState.SavingListData)?.acornCount ?: 0,
                    goldCount = (savingListState as? SavingListState.SavingListData)?.goldAcornCount ?: 0,
                )
                SavingListContent(
                    savingListState = savingListState,
                    onShowDetail = onShowDetail,
                    onExecuteChange = onExecuteChange,
                    modifier = Modifier.weight(1f)
                )
            }
            FadeAnimatedVisibility(
                visible = savingListState is SavingListState.SavingListData,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                if (savingListState is SavingListState.SavingListData) {
                    Column(
                        modifier = Modifier.padding(start = 30.dp, top = 60.dp)
                    ) {
                        Text(
                            text = "${formatDateBasedOnYear(month)} 총 납입금액",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = TypoTheme.typography.headlineSmallM,
                        )
                        Text(
                            text = savingListState.savePlanList.totalString,
                            style = TypoTheme.typography.displaySmallB,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable(onClick = onGoBack)
                    .align(Alignment.TopStart),
            ) {
                TopAppbarIcon(
                    iconId = R.drawable.ic_back,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun SavingListContent(
    savingListState: SavingListState,
    onShowDetail: (Long) -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    FadeAnimatedVisibility(
        savingListState is SavingListState.SavingListData,
        modifier = modifier
    ) {
        if (savingListState is SavingListState.SavingListData) {
            SaveListBody(
                savePlanList = savingListState.savePlanList,
                onShowDetail = onShowDetail,
                onExecuteChange = onExecuteChange,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendingListScreenPreview() {
    JunTheme {
        SavingListScreen(
            savingListState = SavingListState.SavingListData(SavePlanList.sample),
            month = YearMonth.now(),
            onSavingAdd = {},
            onShowDetail = {},
            onGoBack = {},
            onExecuteChange = { _, _ -> },
        )
    }
}