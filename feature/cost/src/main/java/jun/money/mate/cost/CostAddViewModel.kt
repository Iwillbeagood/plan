package jun.money.mate.cost

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.cost.contract.CostAddEffect
import jun.money.mate.cost.contract.CostModalEffect
import jun.money.mate.cost.contract.CostAddState
import jun.money.mate.domain.AddCostUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.CostType
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
import kotlin.math.ceil

@HiltViewModel
internal class CostAddViewModel @Inject constructor(
    private val addCostUsecase: AddCostUsecase
) : ViewModel() {

    var currentStep = mutableStateOf(CostStep.entries.first())
        private set

    var addSteps = mutableStateOf(listOf(CostStep.entries.first()))
        private set

    private val _costAddState = MutableStateFlow(CostAddState())
    val costAddState: StateFlow<CostAddState> get() = _costAddState

    private val _costModalEffect = MutableStateFlow<CostModalEffect>(CostModalEffect.Idle)
    val costModalEffect: StateFlow<CostModalEffect> get() = _costModalEffect

    private val _costAddEffect = MutableSharedFlow<CostAddEffect>()
    val costAddEffect: SharedFlow<CostAddEffect> get() = _costAddEffect.asSharedFlow()

    fun nextStep() {
        val state = costAddState.value
        when (val step = currentStep.value) {
            CostStep.CostType -> {
                if (state.goalAmount <= 0) {
                    showSnackBar(MessageType.Message(step.message))
                    showNumberKeyboard()
                    return
                }
                changeStep(CostStep.Amount)
                dismiss()
            }
            CostStep.Amount -> {
                if (state.amount.isEmpty()) {
                    showSnackBar(MessageType.Message(step.message))
                    return
                }
                changeStep(CostStep.Date)
            }
            CostStep.Date -> {
                addCost()
            }
        }

        removeTextFocus()
        scrollBottom()
    }

    private fun changeStep(step: CostStep) {
        currentStep.value = step
        addSteps.value += step
    }

    private fun addCost() {
        val state = costAddState.value
        viewModelScope.launch {
            addCostUsecase(
                state.amount.toLong(),
                state.costType,
                state.dateType,
                onSuccess = ::complete,
                onError = ::showSnackBar
            )
        }
    }

    fun goalAmountValueChange(value: ValueState) {
        _costAddState.update {
            it.copy(
                goalAmount = value.value(it.goalAmountString).toLongOrNull() ?: 0
            )
        }
    }

    fun costTypeSelected(costType: CostType?) {
        _costAddState.update {
            it.copy(costType = costType)
        }
    }

    fun amountValueChange(value: String) {
        _costAddState.update {
            it.copy(
                amount = value,
                count = calculatePaymentCount(it.goalAmount, value.toLongOrNull() ?: 0).toString()
            )
        }
    }

    fun countValueChange(value: String) {
        _costAddState.update {
            it.copy(
                amount = calculatePaymentAmount(it.goalAmount, value.toIntOrNull() ?: 0).toString(),
                count = value
            )
        }
    }


    fun titleChange(value: String) {
        _costAddState.update {
            it.copy(title = value)
        }
    }

    fun showNumberKeyboard() {
        removeTextFocus()
        _costModalEffect.update { CostModalEffect.ShowNumberKeyboard }
    }

    private fun dismiss() {
        _costModalEffect.update { CostModalEffect.Idle }
    }

    fun numberKeyboardDismiss() {
        nextStep()
        _costModalEffect.update { CostModalEffect.Idle }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _costAddEffect.emit(CostAddEffect.ShowSnackBar(messageType))
        }
    }

    private fun removeTextFocus() {
        viewModelScope.launch {
            _costAddEffect.emit(CostAddEffect.RemoveTextFocus)
        }
    }

    private fun scrollBottom() {
        viewModelScope.launch {
            _costAddEffect.emit(CostAddEffect.ScrollToBottom)
        }
    }

    private fun complete() {
        viewModelScope.launch {
            _costAddEffect.emit(CostAddEffect.CostAddComplete)
        }
    }

    private fun calculatePaymentCount(targetAmount: Long, paymentAmount: Long): Int {
        return ceil(targetAmount.toDouble() / paymentAmount.toDouble()).toInt()
    }

    private fun calculatePaymentAmount(targetAmount: Long, paymentCount: Int): Int {
        return ceil(targetAmount.toDouble() / paymentCount.toDouble()).toInt()
    }
}




