package jun.money.mate.cost

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.cost.contract.CostAddEffect
import jun.money.mate.cost.contract.CostAddState
import jun.money.mate.domain.UpsertCostUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.CostType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CostAddViewModel @Inject constructor(
    private val upsertCostUsecase: UpsertCostUsecase,
) : ViewModel() {

    var currentStep = mutableStateOf(CostStep.entries.first())
        private set

    var addSteps = mutableStateOf(listOf(CostStep.entries.first()))
        private set

    private val _costAddState = MutableStateFlow(CostAddState())
    val costAddState: StateFlow<CostAddState> get() = _costAddState

    private val _costAddEffect = MutableSharedFlow<CostAddEffect>()
    val costAddEffect: SharedFlow<CostAddEffect> get() = _costAddEffect.asSharedFlow()

    fun nextStep() {
        val state = costAddState.value
        when (val step = currentStep.value) {
            CostStep.CostType -> {
                changeStep(CostStep.Amount)
            }
            CostStep.Amount -> {
                if (state.amount <= 0) {
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
    }

    private fun changeStep(step: CostStep) {
        currentStep.value = step
        addSteps.value += step
    }

    private fun popBackStep(toStep: CostStep) {
        currentStep.value = toStep
        addSteps.value = addSteps.value.filter { it.ordinal <= toStep.ordinal }
    }

    private fun addCost() {
        val state = costAddState.value
        viewModelScope.launch {
            upsertCostUsecase(
                amount = state.amount,
                costType = state.costType,
                day = state.day,
                onSuccess = ::complete,
                onError = ::showSnackBar,
            )
        }
    }

    fun costTypeSelected(costType: CostType?) {
        _costAddState.update {
            it.copy(costType = costType)
        }

        if (costType == null) {
            popBackStep(CostStep.CostType)
        } else {
            nextStep()
        }
    }

    fun amountValueChange(value: String) {
        _costAddState.update {
            it.copy(
                amount = value.toLongOrNull() ?: 0,
            )
        }
    }

    fun daySelected(day: String) {
        _costAddState.update {
            it.copy(day = day.toInt())
        }
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

    private fun complete() {
        viewModelScope.launch {
            _costAddEffect.emit(CostAddEffect.CostAddComplete)
        }
    }
}
