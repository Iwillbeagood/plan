package jun.money.mate.save

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jun.money.mate.designsystem.component.BottomToTopSlideFadeAnimatedVisibility
import jun.money.mate.designsystem.component.FadeAnimatedVisibility
import jun.money.mate.designsystem.component.FixedText
import jun.money.mate.designsystem.component.RegularButton
import jun.money.mate.designsystem.component.TextCheckBox
import jun.money.mate.designsystem.component.TextDialog
import jun.money.mate.designsystem.component.TopAppbar
import jun.money.mate.designsystem.component.TopAppbarType
import jun.money.mate.designsystem.component.TopToBottomAnimatedVisibility
import jun.money.mate.designsystem.component.TwoBtnDialog
import jun.money.mate.designsystem.component.UnderLineText
import jun.money.mate.designsystem.component.VerticalSpacer
import jun.money.mate.designsystem.theme.ChangeStatusBarColor
import jun.money.mate.designsystem.theme.JunTheme
import jun.money.mate.designsystem.theme.TypoTheme
import jun.money.mate.designsystem_date.datetimepicker.DayPicker
import jun.money.mate.model.Utils
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavingsType
import jun.money.mate.model.save.SavingsType.Companion.title
import jun.money.mate.save.contract.SaveDetailEffect
import jun.money.mate.save.contract.SaveDetailModalEffect
import jun.money.mate.navigation.interop.LocalNavigateActionInterop
import jun.money.mate.ui.number.NumberKeyboard
import jun.money.mate.navigation.interop.rememberShowSnackBar

@Composable
internal fun SaveDetailRoute(
    viewModel: SaveDetailViewModel = hiltViewModel()
) {
    ChangeStatusBarColor(MaterialTheme.colorScheme.background)

    val showSnackBar = rememberShowSnackBar()
    val navigateAction = LocalNavigateActionInterop.current
    val saveDetailState by viewModel.saveDetailState.collectAsStateWithLifecycle()
    val saveModalEffect by viewModel.modalEffect.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppbar(
                onBackEvent = navigateAction::popBackStack,
                title = "저축 수정",
                navigationType = TopAppbarType.Custom {
                    Text(
                        text = "삭제하기",
                        style = TypoTheme.typography.titleMediumM,
                        modifier = Modifier
                            .clickable(
                                onClick = viewModel::showDeleteDialog
                            )
                            .padding(5.dp)
                        ,
                    )
                }
            )
        },
        bottomBar = {
            BottomToTopSlideFadeAnimatedVisibility(
                visible = viewModel.isEdited.value
            ) {
                RegularButton(
                    text = "수정하기",
                    onClick = viewModel::editSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceDim)
                        .padding(16.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(it)
        ) {
            SaveDetailContent(
                isEdited = viewModel.isEdited.value,
                saveDetailState = saveDetailState,
                viewModel = viewModel
            )
        }
    }

    SaveDetailModalContent(
        modalEffect = saveModalEffect,
        viewModel = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.saveEffect.collect {
            when (it) {
                is SaveDetailEffect.ShowSnackBar -> showSnackBar(it.messageType)
                SaveDetailEffect.SaveDetailComplete -> navigateAction.popBackStack()
            }
        }
    }
}

@Composable
private fun SaveDetailContent(
    isEdited: Boolean,
    saveDetailState: SaveDetailState,
    viewModel: SaveDetailViewModel
) {
    FadeAnimatedVisibility(
        saveDetailState is SaveDetailState.Data
    ) {
        if (saveDetailState is SaveDetailState.Data) {
            SaveDetailScreen(
                isEdited = isEdited,
                uiState = saveDetailState,
                onShowNumberBottomSheet = viewModel::showNumberKeyboard,
                onDaySelected = viewModel::daySelected,
                onSetEditable = viewModel::setEditable,
            )
        }
    }

}

/**
 *  1. 카테고리
 *  2. 이율
 *  3. 납입 기간과 남은 기간 or 횟수
 *  4, 전체 수익
 *  5. 전체 납입 금액
 * */
@Composable
private fun SaveDetailScreen(
    isEdited: Boolean,
    uiState: SaveDetailState.Data,
    onShowNumberBottomSheet: () -> Unit,
    onDaySelected: (Int) -> Unit,
    onSetEditable: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        VerticalSpacer(40.dp)
        SaveDetailField(
            title = "매월 납입금액"
        ) {
            Column {
                UnderLineText(
                    value = uiState.amountString,
                    hint = "선택",
                    modifier = Modifier.clickable(onClick = onShowNumberBottomSheet),
                )
                TopToBottomAnimatedVisibility(uiState.savePlan.amount != 0L) {
                    Column {
                        VerticalSpacer(4.dp)
                        Text(
                            text = uiState.amountWon,
                            style = TypoTheme.typography.labelLargeM,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        SaveDetailField(
            title = "납입 날짜"
        ) {
            Crossfade(
                isEdited
            ) {
                when (it) {
                    true -> {
                        DayPicker(
                            onDaySelected = { selected ->
                                onDaySelected(selected.toInt())
                            },
                            selectedDay = uiState.savePlan.day.toString(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    false -> {
                        UnderLineText(
                            value = "매월 ${uiState.savePlan.day}일",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.clickable(onClick = onSetEditable),
                        )
                    }
                }
            }
        }
        SaveDetailField(
            title = "카테고리",
        ) {
            FixedText(
                value = uiState.savePlan.savingsType.title,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        SaveDetailField(
            visible = uiState.savePlan.savingsType is SavingsType.PeriodType,
            title = "납입기간"
        ) {
            FixedText(
                value = uiState.savePlan.period,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        SaveDetailField(
            title = "전체 납입한 금액"
        ) {
            FixedText(
                value = Utils.formatAmountWon(uiState.savePlan.periodTotal),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        SaveDetailField(
            title = "납입횟수"
        ) {
            FixedText(
                value = uiState.savePlan.getPaidCount().toString() + " 번",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SaveDetailField(
    visible: Boolean = true,
    title: String,
    content: @Composable () -> Unit,
) {
    TopToBottomAnimatedVisibility(visible) {
        Column {
            Text(
                text = title,
                style = TypoTheme.typography.labelLargeM,
            )
            VerticalSpacer(10.dp)
            content()
            VerticalSpacer(30.dp)
        }
    }
}

@Composable
private fun SaveDetailModalContent(
    modalEffect: SaveDetailModalEffect,
    viewModel: SaveDetailViewModel,
) {
    when (modalEffect) {
        SaveDetailModalEffect.Hidden -> {}
        SaveDetailModalEffect.ShowNumberKeyboard -> {
            NumberKeyboard(
                visible = true,
                buttonText = "닫기",
                onChangeNumber = viewModel::amountValueChange,
                onDismissRequest = viewModel::dismiss,
            )
        }

        SaveDetailModalEffect.ShowBasicDeleteConfirmDialog -> {
            var checked by remember { mutableStateOf(false) }
            TwoBtnDialog(
                onDismissRequest = viewModel::dismiss,
                button2Text = "삭제하기",
                button2Click = viewModel::deleteSaving,
                content = {
                    Text(
                        text = "저축을 삭제하시겠습니까?",
                        style = TypoTheme.typography.titleMediumM,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    VerticalSpacer(16.dp)
                    TextCheckBox(
                        checked = checked,
                        text = "다른 납입 내역도 함께 삭제",
                        style = TypoTheme.typography.titleSmallM,
                        onCheckedChange = { checked = it }
                    )
                }
            )
        }
        SaveDetailModalEffect.ShowPeriodDeleteConfirmDialog -> {
            TextDialog(
                content = "저축을 삭제하시겠습니까?",
                onDismissRequest = viewModel::dismiss,
                button2Text = "삭제하기",
                button2Click = viewModel::deleteSaving,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveDetailPreview() {
    JunTheme {
        SaveDetailScreen(
            isEdited = false,
            uiState = SaveDetailState.Data(
                savePlan = SavePlan.sample,
            ),
            onDaySelected = {},
            onShowNumberBottomSheet = {},
            onSetEditable = {},
        )
    }
}