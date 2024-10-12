package jun.money.mate.income

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.domain.AddIncomeUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.IncomeType
import jun.money.mate.utils.currency.CurrencyFormatter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class IncomeAddViewModel @Inject constructor(
    private val addIncomeUsecase: AddIncomeUsecase
) : ViewModel() {

    private val _incomeAddState = MutableStateFlow(IncomeAddState())
    val incomeAddState: StateFlow<IncomeAddState> get() = _incomeAddState

    private val _incomeModalEffect = MutableStateFlow<IncomeModalEffect>(IncomeModalEffect.Idle)
    val incomeModalEffect: StateFlow<IncomeModalEffect> get() = _incomeModalEffect

    private val _incomeAddEffect = MutableSharedFlow<IncomeAddEffect>()
    val incomeAddEffect: SharedFlow<IncomeAddEffect> get() = _incomeAddEffect.asSharedFlow()

    fun onAddIncome() {
        val state = _incomeAddState.value
        viewModelScope.launch {
            addIncomeUsecase(
                title = state.title,
                amount = state.amount,
                type = state.type,
                incomeDate = state.date,
                onSuccess = {
                    showSnackBar(MessageType.Message("수입이 추가되었습니다."))
                    incomeAddComplete()
                },
                onError = ::showSnackBar
            )
        }
    }

    fun onTitleValueChange(value: String) {
        _incomeAddState.update {
            it.copy(title = value)
        }
    }

    fun onAmountValueChange(value: String) {
        _incomeAddState.update {
            it.copy(
                amount = CurrencyFormatter.deFormatAmountDouble(value)
            )
        }
    }

    fun onRegularIncomeClick() {
        _incomeAddState.update {
            it.copy(type = IncomeType.REGULAR)
        }
    }

    fun onVariableIncomeClick() {
        _incomeAddState.update {
            it.copy(type = IncomeType.VARIABLE)
        }
    }

    fun onDateSelected(date: LocalDate) {
        _incomeAddState.update {
            it.copy(date = date)
        }
    }

    fun onShowDatePicker() {
        _incomeModalEffect.update { IncomeModalEffect.ShowDatePicker(_incomeAddState.value.date) }
    }

    fun onDismiss() {
        _incomeModalEffect.update { IncomeModalEffect.Idle }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _incomeAddEffect.emit(IncomeAddEffect.ShowSnackBar(messageType))
        }
    }

    fun incomeAddComplete() {
        viewModelScope.launch {
            _incomeAddEffect.emit(IncomeAddEffect.IncomeAddComplete)
        }
    }
}

@Immutable
internal data class IncomeAddState(
    val title: String = "",
    val amount: Double = 0.0,
    val date: LocalDate = LocalDate.now(),
    val type: IncomeType = IncomeType.REGULAR,
) {

    val regularIncomeSelected get() = type == IncomeType.REGULAR
    val variableIncomeSelected get() = type == IncomeType.VARIABLE

    val amountString get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
}

@Stable
internal sealed interface IncomeAddEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : IncomeAddEffect

    @Immutable
    data object IncomeAddComplete : IncomeAddEffect
}

@Stable
internal sealed interface IncomeModalEffect {

    @Immutable
    data object Idle : IncomeModalEffect

    @Immutable
    data class ShowDatePicker(val date: LocalDate) : IncomeModalEffect
}