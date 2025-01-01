package jun.money.mate.income

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.domain.AddIncomeUsecase
import jun.money.mate.income.IncomeAddState.Companion.uiValue
import jun.money.mate.income.IncomeAddStep.Companion.nextStep
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.IncomeType
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.navigation.utils.toRouteType
import jun.money.mate.ui.number.ValueState
import jun.money.mate.ui.number.ValueState.Companion.value
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

    private val addType = savedStateHandle.toRouteType<MainTabRoute.Income.Add, AddType>().addType

    private val _incomeAddState = MutableStateFlow<IncomeAddState>(IncomeAddState.Loading)
    val incomeAddState: StateFlow<IncomeAddState> = _incomeAddState.onStart {
        init()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
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
                    AddType.New -> IncomeAddState.UiData(
                        id = System.currentTimeMillis(),
                        title = "",
                        amount = 0,
                        date = LocalDate.now(),
                        type = null
                    )

                    is AddType.Edit -> {
                        incomeRepository.getIncomeById(addType.id).let {
                            IncomeAddState.UiData(
                                id = it.id,
                                title = it.title,
                                amount = it.amount,
                                date = it.incomeDate,
                                type = it.type,
                                currentStep = IncomeAddStep.endStep,
                                incomeAddSteps = IncomeAddStep.entries
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onAddIncome() {
        val state = _incomeAddState.uiValue ?: return
        viewModelScope.launch {
            addIncomeUsecase(
                id = state.id,
                title = state.title,
                amount = state.amount,
                type = state.type,
                incomeDate = state.date,
                onSuccess = {
                    showSnackBar(
                        MessageType.Message(
                            "수입이 ${
                                when (addType) {
                                    is AddType.Edit -> "수정"
                                    AddType.New -> "추가"
                                }
                            }되었습니다."
                        )
                    )
                    incomeAddComplete()
                },
                onError = ::showSnackBar
            )
        }
    }

    fun nextStep() {
        if (addType is AddType.Edit) {
            onAddIncome()
            return
        }

        val state = _incomeAddState.uiValue ?: return
        when (state.currentStep) {
            IncomeAddStep.Type -> {
                if (state.type == null) {
                    showSnackBar(MessageType.Message(state.currentStep.message))
                    return
                }
                stepUpdateToNext()
            }
            IncomeAddStep.Title -> {
                if (state.title.isBlank()) {
                    showSnackBar(MessageType.Message(state.currentStep.message))
                    return
                }
                stepUpdateToNext()
                dismissKeyboard()
                showNumberKeyboard()
                removeTitleFocus()
            }
            IncomeAddStep.Amount -> {
                if (state.amount <= 0) {
                    showSnackBar(MessageType.Message(state.currentStep.message))
                    showNumberKeyboard()
                    return
                }
                stepUpdateToNext()
                showDatePicker()
            }
            IncomeAddStep.Date -> {
                onAddIncome()
            }
        }
    }

    private fun stepUpdateToNext() {
        val state = _incomeAddState.uiValue ?: return
        val nextStep = state.currentStep.nextStep()
        _incomeAddState.update {
            state.copy(
                currentStep = nextStep,
                incomeAddSteps = state.incomeAddSteps + listOf(nextStep)
            )
        }
    }


    fun onTitleValueChange(value: String) {
        val state = _incomeAddState.uiValue ?: return

        _incomeAddState.update {
            state.copy(title = value)
        }
    }

    fun amountValueChange(value: ValueState) {
        val state = _incomeAddState.uiValue ?: return

        _incomeAddState.update {
            state.copy(
                amount = value.value(state.amountString).toLongOrNull() ?: 0
            )
        }
    }

    fun onDateSelected(date: LocalDate) {
        val state = _incomeAddState.uiValue ?: return

        _incomeAddState.update {
            state.copy(date = date)
        }

        nextStep()
    }

    fun incomeTypeSelected(incomeType: IncomeType) {
        val state = _incomeAddState.uiValue ?: return

        _incomeAddState.update {
            state.copy(type = incomeType)
        }

        dismiss()
        nextStep()
    }

    fun showDatePicker() {
        val state = _incomeAddState.uiValue ?: return

        _incomeModalEffect.update { IncomeModalEffect.ShowDatePicker(state.date) }
    }

    fun showTypePicker() {
        _incomeModalEffect.update { IncomeModalEffect.ShowTypePicker }
    }

    fun showNumberKeyboard() {
        _incomeModalEffect.update { IncomeModalEffect.ShowNumberKeyboard }
    }

    private fun dismissKeyboard() {
        viewModelScope.launch {
            _incomeAddEffect.emit(IncomeAddEffect.DismissKeyboard)
        }
    }

    private fun removeTitleFocus() {
        viewModelScope.launch {
            _incomeAddEffect.emit(IncomeAddEffect.RemoveTitleFocus)
        }
    }

    fun dismiss() {
        _incomeModalEffect.update { IncomeModalEffect.Idle }
    }

    fun numberKeyboardDismiss() {
        nextStep()
        _incomeModalEffect.update { IncomeModalEffect.Idle }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _incomeAddEffect.emit(IncomeAddEffect.ShowSnackBar(messageType))
        }
    }

    private fun incomeAddComplete() {
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
    data class UiData(
        val id: Long,
        val title: String,
        val amount: Long,
        val date: LocalDate,
        val type: IncomeType?,
        val currentStep : IncomeAddStep = IncomeAddStep.startStep,
        val incomeAddSteps: List<IncomeAddStep> = listOf(IncomeAddStep.startStep)
    ) : IncomeAddState {

        val amountString get() = if (amount > 0) amount.toString() else ""
        val amountWon get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
    }

    companion object {

        val MutableStateFlow<IncomeAddState>.uiValue get() = this.value as? UiData

        fun IncomeAddState.buttonText() = when (this) {
            is UiData -> {
                when(this.currentStep) {
                    IncomeAddStep.Date -> "완료"
                    else -> "다음"
                }
            }
            Loading -> "다음"
        }
    }
}

@Stable
internal sealed interface IncomeAddEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : IncomeAddEffect

    @Immutable
    data object IncomeAddComplete : IncomeAddEffect

    @Immutable
    data object DismissKeyboard : IncomeAddEffect

    @Immutable
    data object RemoveTitleFocus : IncomeAddEffect
}

@Stable
internal sealed interface IncomeModalEffect {

    @Immutable
    data object Idle : IncomeModalEffect

    @Immutable
    data object ShowTypePicker : IncomeModalEffect

    @Immutable
    data class ShowDatePicker(val date: LocalDate) : IncomeModalEffect

    @Immutable
    data object ShowNumberKeyboard : IncomeModalEffect
}