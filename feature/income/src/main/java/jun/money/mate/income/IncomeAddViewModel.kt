package jun.money.mate.income

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.domain.AddIncomeUsecase
import jun.money.mate.income.contract.EditState
import jun.money.mate.income.contract.IncomeAddState
import jun.money.mate.income.contract.IncomeEffect
import jun.money.mate.income.contract.IncomeModalEffect
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.ui.number.ValueState
import jun.money.mate.ui.number.ValueState.Companion.value
import jun.money.mate.utils.flow.updateWithData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
internal class IncomeAddViewModel @Inject constructor(
    private val addIncomeUsecase: AddIncomeUsecase,
) : ViewModel() {

    var addStep = mutableStateOf(IncomeAddStep.Title)
        private set

    var addSteps = mutableStateOf(listOf(IncomeAddStep.Title))
        private set

    private val _incomeAddState = MutableStateFlow(IncomeAddState())
    val incomeAddState: StateFlow<IncomeAddState> get() = _incomeAddState

    private val _incomeModalEffect = MutableStateFlow<IncomeModalEffect>(IncomeModalEffect.Idle)
    val incomeModalEffect: StateFlow<IncomeModalEffect> get() = _incomeModalEffect

    private val _incomeEffect = MutableSharedFlow<IncomeEffect>()
    val incomeEffect: SharedFlow<IncomeEffect> get() = _incomeEffect.asSharedFlow()

    private fun addIncome() {
        val state = incomeAddState.value

        viewModelScope.launch {
            addIncomeUsecase(
                title = state.title,
                amount = state.amount,
                dateType = state.dateType,
                day = state.date,
                onSuccess = {
                    showSnackBar(MessageType.Message("수입이 추가되었습니다."))
                    incomeAddComplete()
                },
                onError = ::showSnackBar,
            )
        }
    }

    fun nextStep() {
        val state = incomeAddState.value
        when (val step = addStep.value) {
            IncomeAddStep.Title -> {
                if (state.title.isBlank()) {
                    showSnackBar(MessageType.Message(step.message))
                    return
                }

                changeStep(IncomeAddStep.Amount)
                dismissKeyboard()
                showNumberKeyboard()
                removeTitleFocus()
            }
            IncomeAddStep.Amount -> {
                if (state.amount <= 0) {
                    showSnackBar(MessageType.Message(step.message))
                    showNumberKeyboard()
                    return
                }
                changeStep(IncomeAddStep.Type)
                dismiss()
            }
            IncomeAddStep.Type -> {
                dismissKeyboard()

                if (state.dateType == null) {
                    showSnackBar(MessageType.Message(step.message))
                    return
                }
                addIncome()
            }
        }
    }

    private fun changeStep(step: IncomeAddStep) {
        addStep.value = step
        addSteps.value += step
    }

    fun titleValueChange(value: String) {
        _incomeAddState.update {
            it.copy(title = value)
        }
    }

    fun amountValueChange(value: ValueState) {
        _incomeAddState.update {
            it.copy(
                amount = value.value(it.amountString).toLongOrNull() ?: 0,
            )
        }
    }

    fun daySelected(day: Int) {
        _incomeAddState.update {
            it.copy(date = day)
        }
    }

    fun dateTypeSelected(dateType: DateType) {
        _incomeAddState.update {
            it.copy(dateType = dateType)
        }
    }

    fun showNumberKeyboard() {
        dismissKeyboard()
        _incomeModalEffect.update { IncomeModalEffect.ShowNumberKeyboard }
    }

    private fun dismissKeyboard() {
        viewModelScope.launch {
            _incomeEffect.emit(IncomeEffect.DismissKeyboard)
        }
    }

    private fun removeTitleFocus() {
        viewModelScope.launch {
            _incomeEffect.emit(IncomeEffect.RemoveTitleFocus)
        }
    }

    fun dismiss() {
        _incomeModalEffect.update { IncomeModalEffect.Idle }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _incomeEffect.emit(IncomeEffect.ShowSnackBar(messageType))
        }
    }

    private fun incomeAddComplete() {
        viewModelScope.launch {
            _incomeEffect.emit(IncomeEffect.IncomeComplete)
        }
    }
}
