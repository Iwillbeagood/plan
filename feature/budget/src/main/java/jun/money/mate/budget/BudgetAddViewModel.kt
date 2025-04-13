package jun.money.mate.budget

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.budget.contract.BudgetAddEffect
import jun.money.mate.budget.contract.BudgetAddModalEffect
import jun.money.mate.budget.contract.BudgetAddUiState
import jun.money.mate.budget.navigation.NAV_NAME
import jun.money.mate.domain.AddBudgetUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.ui.number.ValueState
import jun.money.mate.ui.number.ValueState.Companion.value
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BudgetAddViewModel @Inject constructor(
    private val addBudgetUsecase: AddBudgetUsecase
) : ViewModel() {

    var addStep = mutableStateOf(BudgetAddStep.Title)
        private set

    var addSteps = mutableStateOf(listOf(BudgetAddStep.Title))
        private set

    private val _budgetAddUiState = MutableStateFlow(BudgetAddUiState())
    val budgetAddUiState: StateFlow<BudgetAddUiState>  get() = _budgetAddUiState

    private val _budgetAddModalEffect = MutableStateFlow<BudgetAddModalEffect>(BudgetAddModalEffect.Idle)
    val budgetAddModalEffect: StateFlow<BudgetAddModalEffect> get() = _budgetAddModalEffect

    private val _budgetAddEffect = MutableSharedFlow<BudgetAddEffect>()
    val budgetAddEffect: SharedFlow<BudgetAddEffect> get() = _budgetAddEffect.asSharedFlow()

    private fun addBudget() {
        val state = budgetAddUiState.value

        viewModelScope.launch {
            addBudgetUsecase(
                title = state.title,
                budget = state.budget,
                onSuccess = {
                    showSnackBar(MessageType.Message("${NAV_NAME}이 설정되었습니다."))
                    addComplete()
                },
                onError = ::showSnackBar
            )
        }
    }

    fun nextStep() {
        val state = budgetAddUiState.value
        when (val step = addStep.value) {
            BudgetAddStep.Title -> {
                if (state.title.isBlank()) {
                    showSnackBar(MessageType.Message(step.message))
                    return
                }

                changeStep(BudgetAddStep.Amount)
                dismissKeyboard()
                showNumberKeyboard()
                removeTitleFocus()
            }
            BudgetAddStep.Amount -> {
                if (state.budget <= 0) {
                    showSnackBar(MessageType.Message(step.message))
                    showNumberKeyboard()
                    return
                }
                dismiss()
                addBudget()
            }
        }
    }

    private fun changeStep(step: BudgetAddStep) {
        addStep.value = step
        addSteps.value += step
    }

    fun titleValueChange(value: String) {
        _budgetAddUiState.update {
            it.copy(title = value)
        }
    }

    fun amountValueChange(value: ValueState) {
        _budgetAddUiState.update {
            it.copy(
                budget = value.value(it.amountString).toLongOrNull() ?: 0
            )
        }
    }
    fun showNumberKeyboard() {
        _budgetAddModalEffect.update { BudgetAddModalEffect.ShowNumberKeyboard }
    }

    private fun dismissKeyboard() {
        viewModelScope.launch {
            _budgetAddEffect.emit(BudgetAddEffect.DismissKeyboard)
        }
    }

    private fun removeTitleFocus() {
        viewModelScope.launch {
            _budgetAddEffect.emit(BudgetAddEffect.RemoveTitleFocus)
        }
    }

    fun dismiss() {
        _budgetAddModalEffect.update { BudgetAddModalEffect.Idle }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _budgetAddEffect.emit(BudgetAddEffect.ShowSnackBar(messageType))
        }
    }

    private fun addComplete() {
        viewModelScope.launch {
            _budgetAddEffect.emit(BudgetAddEffect.BudgetAddComplete)
        }
    }
}

