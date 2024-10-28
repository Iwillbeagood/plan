package jun.money.mate.income

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.domain.AddIncomeUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.IncomeType
import jun.money.mate.navigation.Route
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.navigation.utils.toRouteType
import jun.money.mate.utils.currency.CurrencyFormatter
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class IncomeAddViewModel @Inject constructor(
    private val addIncomeUsecase: AddIncomeUsecase,
    private val incomeRepository: IncomeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val addType = savedStateHandle.toRouteType<Route.Income.Add, AddType>().addType

    private val _incomeAddState = MutableStateFlow<IncomeAddState>(IncomeAddState.Loading)
    val incomeAddState: StateFlow<IncomeAddState> = _incomeAddState.onStart {
       init()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = IncomeAddState.Loading
    )

    private val _incomeModalEffect = MutableStateFlow<IncomeModalEffect>(IncomeModalEffect.Idle)
    val incomeModalEffect: StateFlow<IncomeModalEffect> get() = _incomeModalEffect

    private val _incomeAddEffect = MutableSharedFlow<IncomeAddEffect>()
    val incomeAddEffect: SharedFlow<IncomeAddEffect> get() = _incomeAddEffect.asSharedFlow()

    private fun init() {
        viewModelScope.launch {
            _incomeAddState.update {
                when (addType) {
                    AddType.New -> IncomeAddState.IncomeData(
                        title = "",
                        amount = 0.0,
                        date = LocalDate.now(),
                        type = IncomeType.REGULAR
                    )
                    is AddType.Edit -> {
                        incomeRepository.getIncomeById(addType.id).let {
                            IncomeAddState.IncomeData(
                                title = it.title,
                                amount = it.amount,
                                date = it.incomeDate,
                                type = it.type
                            )
                        }
                    }
                }
            }
        }
    }

    fun onAddIncome() {
        val state = _incomeAddState.value as? IncomeAddState.IncomeData ?: return
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
        val state = _incomeAddState.value as? IncomeAddState.IncomeData ?: return

        _incomeAddState.update {
            state.copy(title = value)
        }
    }

    fun onAmountValueChange(value: String) {
        val state = _incomeAddState.value as? IncomeAddState.IncomeData ?: return

        _incomeAddState.update {
            state.copy(
                amount = CurrencyFormatter.deFormatAmountDouble(value)
            )
        }
    }

    fun onRegularIncomeClick() {
        val state = _incomeAddState.value as? IncomeAddState.IncomeData ?: return
        _incomeAddState.update {
            state.copy(type = IncomeType.REGULAR)
        }
    }

    fun onVariableIncomeClick() {
        val state = _incomeAddState.value as? IncomeAddState.IncomeData ?: return
        _incomeAddState.update {
            state.copy(type = IncomeType.VARIABLE)
        }
    }

    fun onDateSelected(date: LocalDate) {
        val state = _incomeAddState.value as? IncomeAddState.IncomeData ?: return
        _incomeAddState.update {
            state.copy(date = date)
        }
    }

    fun onShowDatePicker() {
        val state = _incomeAddState.value as? IncomeAddState.IncomeData ?: return

        _incomeModalEffect.update { IncomeModalEffect.ShowDatePicker(state.date) }
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

@Stable
internal sealed interface IncomeAddState {

    @Immutable
    data object Loading : IncomeAddState

    @Immutable
    data class IncomeData(
        val title: String,
        val amount: Double,
        val date: LocalDate,
        val type: IncomeType,
    ) : IncomeAddState {

        val regularIncomeSelected get() = type == IncomeType.REGULAR
        val variableIncomeSelected get() = type == IncomeType.VARIABLE

        val amountString get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
    }
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