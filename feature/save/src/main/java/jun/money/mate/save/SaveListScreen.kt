package jun.money.mate.save

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.etc.EmptyMessage
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.Orange1
import jun.money.mate.model.etc.ViewMode
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.save.component.SaveListBody
import jun.money.mate.ui.DefaultScaffold

@Composable
internal fun SaveListRoute(
    onShowSavingAdd: () -> Unit,
    onShowSavingEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: SavingListViewModel = hiltViewModel()
) {
    val savingListState by viewModel.savingListState.collectAsStateWithLifecycle()
    val viewMode by viewModel.savingViewMode.collectAsStateWithLifecycle()

    SavingListScreen(
        savingListState = savingListState,
        viewMode = viewMode,
        onSavePlanClick = viewModel::changeSavePlanSelected,
        onSavingAdd = onShowSavingAdd,
        onSavingEdit = viewModel::editSave,
        onSavingDelete = viewModel::deleteSave,
        onExecuteChange = viewModel::executeChange,
    )

    LaunchedEffect(Unit) {
        viewModel.savingListEffect.collect { effect ->
            when (effect) {
                is SavingListEffect.EditSpendingPlan -> onShowSavingEdit(effect.id)
                is SavingListEffect.ShowSnackBar -> onShowSnackBar(effect.messageType)
            }
        }
    }
}

@Composable
private fun SavingListScreen(
    savingListState: SavingListState,
    viewMode: ViewMode,
    onSavePlanClick: (SavePlan) -> Unit,
    onSavingAdd: () -> Unit,
    onSavingEdit: () -> Unit,
    onSavingDelete: () -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
) {
    DefaultScaffold(
        color = Orange1,
        bottomBarVisible = viewMode == ViewMode.EDIT,
        addButtonVisible = viewMode == ViewMode.LIST,
        onAdd = onSavingAdd,
        onEdit = onSavingEdit,
        onDelete = onSavingDelete,
        containerColor = MaterialTheme.colorScheme.surfaceDim
    ) {
        SavingListContent(
            savingListState = savingListState,
            onSavePlanClick = onSavePlanClick,
            onExecuteChange = onExecuteChange,
        )
    }
}

@Composable
private fun SavingListContent(
    savingListState: SavingListState,
    onSavePlanClick: (SavePlan) -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
) {
    FadeAnimatedVisibility(savingListState is SavingListState.Empty) {
        if (savingListState is SavingListState.Empty) {
            EmptyMessage("저금이 없습니다.")
        }
    }

    FadeAnimatedVisibility(savingListState is SavingListState.SavingListData) {
        if (savingListState is SavingListState.SavingListData) {
            SaveListBody(
                savePlanList = savingListState.savePlanList,
                onSavePlanClick = onSavePlanClick,
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
            onSavingAdd = {},
            onSavingEdit = {},
            onSavingDelete = {},
            onSavePlanClick = {},
            onExecuteChange = { _, _ -> },
            viewMode = ViewMode.EDIT
        )
    }
}