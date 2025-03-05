package jun.money.mate.save

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.domain.AddSaveUsecase
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SavingsType
import jun.money.mate.ui.number.ValueState
import jun.money.mate.ui.number.ValueState.Companion.value
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
internal class SaveAddViewModel @Inject constructor(
    private val addSaveUsecase: AddSaveUsecase,
    private val saveRepository: SaveRepository,
) : ViewModel() {

    var addStep = mutableStateOf(SaveAddStep.entries.first())
        private set

    var addSteps = mutableStateOf(listOf(SaveAddStep.entries.first()))
        private set

    private val _saveAddState = MutableStateFlow(SaveAddState())
    val saveAddState: StateFlow<SaveAddState> get() = _saveAddState

    private val _saveModalEffect = MutableStateFlow<SaveModalEffect>(SaveModalEffect.Idle)
    val saveModalEffect: StateFlow<SaveModalEffect> get() = _saveModalEffect

    private val _saveAddEffect = MutableSharedFlow<SaveAddEffect>()
    val saveAddEffect: SharedFlow<SaveAddEffect> get() = _saveAddEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            saveRepository.resetExecuteState()
        }
    }

    fun nextStep() {
        val state = saveAddState.value
        when (val step = addStep.value) {
            SaveAddStep.Category -> {
                changeStep(SaveAddStep.Amount)
                showNumberKeyboard()
            }
            SaveAddStep.Amount -> {
                if (state.category == null) {
                    backToStep(SaveAddStep.Category)
                    dismiss()
                    return
                }

                if (state.amount <= 0) {
                    showSnackBar(MessageType.Message(step.message))
                    showNumberKeyboard()
                    return
                }

                changeStep(SaveAddStep.Type)
                dismiss()
            }
            SaveAddStep.Type -> {
                if (state.dateType == null) {
                    showSnackBar(MessageType.Message(step.message))
                    return
                }
                addSave()
            }

        }
    }

    private fun changeStep(step: SaveAddStep) {
        addStep.value = step
        addSteps.value += step
    }

    private fun backToStep(step: SaveAddStep) {
        addSteps.value -= addStep.value
        addStep.value = step
    }

    private fun addSave() {
        val state = saveAddState.value
        viewModelScope.launch {
            addSaveUsecase(
                amount = state.amount,
                planDay = 1,
                category = null,
                onSuccess = {
                    showSnackBar(MessageType.Message("저축 계획이 추가되었습니다."))
                    incomeAddComplete()
                },
                onError = ::showSnackBar
            )
        }
    }

    fun amountValueChange(value: ValueState) {
        _saveAddState.update {
            it.copy(
                amount = value.value(it.amountString).toLongOrNull() ?: 0
            )
        }
    }

    fun dateSelected(date: LocalDate) {
        _saveAddState.update {
            it.copy(dateType = DateType.Specific(date))
        }
    }

    fun daySelected(day: String) {
        _saveAddState.update {
            it.copy(dateType = DateType.Monthly(day.toInt()))
        }
    }

    fun onDateSelected(date: LocalDate) {
        nextStep()
    }

    fun showDatePicker() {
    }

    fun categorySelected(savingsType: SavingsType?) {
        _saveAddState.update { it.copy(category = savingsType) }

        nextStep()
    }

    fun showNumberKeyboard() {
        _saveModalEffect.update { SaveModalEffect.ShowNumberKeyboard }
    }

    fun dismiss() {
        _saveModalEffect.update { SaveModalEffect.Idle }
    }

    private fun removeTextFocus() {
        viewModelScope.launch {
            _saveAddEffect.emit(SaveAddEffect.RemoveTextFocus)
        }
    }

    fun numberKeyboardDismiss() {
        nextStep()
        _saveModalEffect.update { SaveModalEffect.Idle }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _saveAddEffect.emit(SaveAddEffect.ShowSnackBar(messageType))
        }
    }

    private fun incomeAddComplete() {
        viewModelScope.launch {
            _saveAddEffect.emit(SaveAddEffect.SaveAddComplete)
        }
    }
}

@Stable
internal data class SaveAddState(
    val amount: Long = 0,
    val dateType: DateType? = null,
    val category: SavingsType? = null,
) {

    val amountString get() = if (amount > 0) amount.toString() else ""
    val amountWon get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
}

@Stable
internal sealed interface SaveAddEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : SaveAddEffect

    @Immutable
    data object SaveAddComplete : SaveAddEffect

    @Immutable
    data object RemoveTextFocus : SaveAddEffect
}

@Stable
internal sealed interface SaveModalEffect {

    @Immutable
    data object Idle : SaveModalEffect

    @Immutable
    data class ShowDatePicker(val date: LocalDate) : SaveModalEffect

    @Immutable
    data object ShowNumberKeyboard : SaveModalEffect
}