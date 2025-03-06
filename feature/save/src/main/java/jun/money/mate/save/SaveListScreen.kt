package jun.money.mate.save

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.R
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.TopAppbarIcon
import jun.money.mate.designsystem.component.TwoBtnDialog
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem.theme.main10
import jun.money.mate.model.etc.EditMode
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.save.component.AcornBox
import jun.money.mate.save.component.SaveListBody
import jun.money.mate.save.contract.SaveModalEffect
import jun.money.mate.save.contract.SavingListEffect
import jun.money.mate.save.contract.SavingListState
import jun.money.mate.ui.EditModeButton
import java.time.LocalDate

@Composable
internal fun SaveListRoute(
    onGoBack: () -> Unit,
    onShowSavingAdd: () -> Unit,
    onShowSavingEdit: (id: Long) -> Unit,
    onShowSnackBar: (MessageType) -> Unit,
    viewModel: SavingListViewModel = hiltViewModel()
) {
    val savingListState by viewModel.savingListState.collectAsStateWithLifecycle()
    val modalEffect by viewModel.modalEffect.collectAsStateWithLifecycle()
    val month by viewModel.month.collectAsStateWithLifecycle()

    SavingListScreen(
        savingListState = savingListState,
        month = month,
        onPrev = viewModel::prevMonth,
        onNext = viewModel::nextMonth,
        onShowDetail = onShowSavingEdit,
        onSavePlanClick = viewModel::changeSavePlanSelected,
        onSavingAdd = onShowSavingAdd,
        onSavingEdit = viewModel::editSave,
        onDelete = viewModel::showDeleteDialog,
        onExecuteChange = viewModel::executeChange,
        onGoBack = onGoBack,
    )

    ModalContent(modalEffect, viewModel)

    BackHandler {
        val state = savingListState
        if (state is SavingListState.SavingListData) {
            when (state.editMode) {
                EditMode.LIST -> onGoBack()
                else -> viewModel.unselectAll()
            }
        } else {
            onGoBack()
        }
    }

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
    month: LocalDate,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onShowDetail: (Long) -> Unit,
    onSavePlanClick: (Long) -> Unit,
    onSavingAdd: () -> Unit,
    onSavingEdit: () -> Unit,
    onDelete: () -> Unit,
    onExecuteChange: (Boolean, Long) -> Unit,
    onGoBack: () -> Unit,

) {
    Scaffold(
        bottomBar = {
            EditModeButton(
                editMode = (savingListState as? SavingListState.SavingListData)?.editMode ?: EditMode.LIST,
                onAdd = onSavingAdd,
                onDelete = onDelete,
                onEdit = onSavingEdit,
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
                    modifier = Modifier.weight(4f)
                )
                SavingListContent(
                    savingListState = savingListState,
                    month = month,
                    onPrev = onPrev,
                    onNext = onNext,
                    onShowDetail = onShowDetail,
                    onSavePlanClick = onSavePlanClick,
                    onExecuteChange = onExecuteChange,
                    modifier = Modifier.weight(6f)
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
                            text = "이번 달의 저축금액",
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
    month: LocalDate,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onShowDetail: (Long) -> Unit,
    onSavePlanClick: (Long) -> Unit,
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
                month = month,
                onPrev = onPrev,
                onNext = onNext,
                onShowDetail = onShowDetail,
                onSavePlanClick = onSavePlanClick,
                onExecuteChange = onExecuteChange,
            )
        }
    }
}

@Composable
private fun ModalContent(
    modalEffect: SaveModalEffect,
    viewModel: SavingListViewModel
) {
   when (modalEffect) {
        is SaveModalEffect.Hidden -> {}
       SaveModalEffect.ShowDeleteConfirmDialog -> {
           TwoBtnDialog(
               onDismissRequest = viewModel::hideModal,
               button2Text = stringResource(id = jun.money.mate.res.R.string.btn_yes),
               button2Click = viewModel::deleteSave,
               content = {
                   Text(
                       text = "선택한 저축을 삭제하시겠습니까?",
                       style = TypoTheme.typography.titleMediumM
                   )
               }
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
            month = LocalDate.now(),
            onPrev = {},
            onNext = {},
            onSavingAdd = {},
            onSavingEdit = {},
            onShowDetail = {},
            onSavePlanClick = {},
            onGoBack = {},
            onDelete = {},
            onExecuteChange = { _, _ -> },
        )
    }
}