package jun.money.mate.budget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.budget.contract.BudgetDetailEffect
import jun.money.mate.budget.contract.BudgetDetailModalState
import jun.money.mate.budget.contract.BudgetDetailState
import jun.money.mate.budget.navigation.NAV_NAME
import jun.money.mate.dataApi.database.BudgetRepository
import jun.money.mate.model.consumption.Used
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.utils.flow.withData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BudgetDetailViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val id = savedStateHandle.toRoute<MainTabRoute.Budget.Detail>().id

    private val _budgetDetailState = MutableStateFlow<BudgetDetailState>(BudgetDetailState.Loading)
    val budgetDetailState: StateFlow<BudgetDetailState> = _budgetDetailState.onStart {
        fetchBudget()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = BudgetDetailState.Loading,
    )

    private val _budgetDetailEffect = MutableSharedFlow<BudgetDetailEffect>()
    val budgetDetailEffect: SharedFlow<BudgetDetailEffect> get() = _budgetDetailEffect.asSharedFlow()

    private val _modalState = MutableStateFlow<BudgetDetailModalState>(BudgetDetailModalState.Hidden)
    val modalState: StateFlow<BudgetDetailModalState> get() = _modalState

    private fun fetchBudget() {
        viewModelScope.launch {
            budgetRepository.getBudgetFlow(id)
                .collect { budget ->
                    _budgetDetailState.update { BudgetDetailState.BudgetDetailData(budget) }
                }
        }
    }

    fun selectUsed(used: Used) {
        budgetDetailState.withData<BudgetDetailState.BudgetDetailData> { state ->
            viewModelScope.launch {
                _budgetDetailState.update {
                    state.copy(
                        budget = state.budget.copy(
                            usedList = state.budget.usedList.map {
                                it.copy(
                                    isSelected = if (it.id == used.id) {
                                        !it.isSelected
                                    } else {
                                        it.isSelected
                                    },
                                )
                            },
                        ),
                    )
                }
            }
        }
    }

    fun unSelectUsed() {
        budgetDetailState.withData<BudgetDetailState.BudgetDetailData> { state ->
            viewModelScope.launch {
                _budgetDetailState.update {
                    state.copy(
                        budget = state.budget.copy(
                            usedList = state.budget.usedList.map { it.copy(isSelected = false) },
                        ),
                    )
                }
            }
        }
    }

    fun deleteUsed() {
        budgetDetailState.withData<BudgetDetailState.BudgetDetailData> { state ->
            viewModelScope.launch {
                budgetRepository.deleteUsed(*state.selectedUsed.toLongArray())
                showSnackBar(MessageType.Message("사용 내역을 삭제했습니다."))
            }
        }
    }

    fun editUsed() {
        budgetDetailState.withData<BudgetDetailState.BudgetDetailData> { state ->
            viewModelScope.launch {
                if (state.selectedCount != 1) return@launch
                val selectedUsed = state.budget.usedList.find { it.isSelected } ?: return@launch
                showEditUsedSheet(selectedUsed)
            }
        }
    }

    fun editBudget(budget: String) {
        budgetDetailState.withData<BudgetDetailState.BudgetDetailData> {
            viewModelScope.launch {
                budgetRepository.upsert(it.budget.copy(budget = budget.toLongOrNull() ?: 0L))
                showSnackBar(MessageType.Message("${NAV_NAME}을 수정했습니다."))
            }
        }
    }

    fun deleteBudget() {
        viewModelScope.launch {
            budgetRepository.deleteBudget(id)
            showSnackBar(MessageType.Message("${NAV_NAME}을 삭제했습니다."))
            hideModal()
            _budgetDetailEffect.emit(BudgetDetailEffect.PopBackStack)
        }
    }

    fun addUsed(used: Used) {
        budgetDetailState.withData<BudgetDetailState.BudgetDetailData> {
            viewModelScope.launch {
                budgetRepository.insertUsed(
                    used.copy(
                        budgetId = it.budget.id,
                    ),
                )
                showSnackBar(MessageType.Message("사용 내역을 추가했습니다."))
            }
        }
    }

    fun editUsed(used: Used) {
        budgetDetailState.withData<BudgetDetailState.BudgetDetailData> {
            viewModelScope.launch {
                budgetRepository.updateUsed(
                    used.copy(
                        budgetId = it.budget.id,
                    ),
                )
                showSnackBar(MessageType.Message("사용 내역을 수정했습니다."))
            }
        }
    }

    fun showEditBudgetSheet() {
        viewModelScope.launch {
            _modalState.update { BudgetDetailModalState.ShowEditBudgetSheet() }
        }
    }

    fun showEditBudgetSheetByFeedback() {
        budgetDetailState.withData<BudgetDetailState.BudgetDetailData> { state ->
            viewModelScope.launch {
                _modalState.update { BudgetDetailModalState.ShowEditBudgetSheet(state.budget.maxUse) }
            }
        }
    }

    fun showAddUsedSheet() {
        viewModelScope.launch {
            _modalState.update { BudgetDetailModalState.ShowAddUsedSheet }
        }
    }

    fun showEditUsedSheet(used: Used) {
        viewModelScope.launch {
            _modalState.update { BudgetDetailModalState.ShowEditUsedSheet(used) }
        }
    }

    fun showDeleteDialog() {
        viewModelScope.launch {
            _modalState.update { BudgetDetailModalState.ShowDeleteDialog }
        }
    }

    fun hideModal() {
        viewModelScope.launch {
            _modalState.emit(BudgetDetailModalState.Hidden)
        }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _budgetDetailEffect.emit(BudgetDetailEffect.ShowSnackBar(messageType))
        }
    }
}
