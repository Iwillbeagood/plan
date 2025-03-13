package jun.money.mate.challenge

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.domain.AddSaveUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SavingsType
import jun.money.mate.challenge.contract.SaveAddEffect
import jun.money.mate.challenge.contract.SaveAddState
import jun.money.mate.challenge.contract.SaveAddModalEffect
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
internal class ChallengeAddViewModel @Inject constructor(
    private val addSaveUsecase: AddSaveUsecase
) : ViewModel() {

    var addStep = mutableStateOf(SaveAddStep.entries.first())
        private set

    var addSteps = mutableStateOf(listOf(SaveAddStep.entries.first()))
        private set

    private val _saveAddState = MutableStateFlow(SaveAddState())
    val saveAddState: StateFlow<SaveAddState> get() = _saveAddState

    private val _saveAddModalEffect = MutableStateFlow<SaveAddModalEffect>(SaveAddModalEffect.Idle)
    val saveAddModalEffect: StateFlow<SaveAddModalEffect> get() = _saveAddModalEffect

    private val _saveAddEffect = MutableSharedFlow<SaveAddEffect>()
    val saveAddEffect: SharedFlow<SaveAddEffect> get() = _saveAddEffect.asSharedFlow()

    init {
        viewModelScope.launch {
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
            SaveAddStep.Type -> addSave()
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
                day = state.day,
                category = state.category,
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

    fun daySelected(day: String) {
        _saveAddState.update {
            it.copy(day = day.toInt())
        }
    }

    fun categorySelected(savingsType: SavingsType?) {
        _saveAddState.update { it.copy(category = savingsType) }

        nextStep()
    }

    fun showNumberKeyboard() {
        _saveAddModalEffect.update { SaveAddModalEffect.ShowNumberKeyboard }
    }

    private fun dismiss() {
        _saveAddModalEffect.update { SaveAddModalEffect.Idle }
    }

    fun numberKeyboardDismiss() {
        nextStep()
        _saveAddModalEffect.update { SaveAddModalEffect.Idle }
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




