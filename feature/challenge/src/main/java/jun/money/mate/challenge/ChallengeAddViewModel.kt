package jun.money.mate.challenge

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.challenge.contract.ChallengeAddEffect
import jun.money.mate.challenge.contract.ChallengeAddState
import jun.money.mate.challenge.contract.ChallengeModalEffect
import jun.money.mate.domain.AddChallengeUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.ChallengeType
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
internal class ChallengeAddViewModel @Inject constructor(
    private val addChallengeUsecase: AddChallengeUsecase,
) : ViewModel() {

    var currentStep = mutableStateOf(ChallengeStep.entries.first())
        private set

    var addSteps = mutableStateOf(listOf(ChallengeStep.entries.first()))
        private set

    private val _challengeAddState = MutableStateFlow(ChallengeAddState())
    val challengeAddState: StateFlow<ChallengeAddState> get() = _challengeAddState

    private val _challengeModalEffect = MutableStateFlow<ChallengeModalEffect>(ChallengeModalEffect.Idle)
    val challengeModalEffect: StateFlow<ChallengeModalEffect> get() = _challengeModalEffect

    private val _challengeAddEffect = MutableSharedFlow<ChallengeAddEffect>()
    val challengeAddEffect: SharedFlow<ChallengeAddEffect> get() = _challengeAddEffect.asSharedFlow()

    fun nextStep() {
        val state = challengeAddState.value
        when (val step = currentStep.value) {
            ChallengeStep.GoalAmount -> {
                if (state.goalAmount <= 0) {
                    showSnackBar(MessageType.Message(step.message))
                    showNumberKeyboard()
                    return
                }
                changeStep(ChallengeStep.AmountCount)
                dismiss()
            }
            ChallengeStep.AmountCount -> {
                if (state.amount.isEmpty()) {
                    showSnackBar(MessageType.Message(step.message))
                    return
                }
                changeStep(ChallengeStep.Remaining)
            }
            ChallengeStep.Remaining -> {
                if (state.challengeType == null) {
                    showSnackBar(MessageType.Message(step.message))
                    return
                }
                addChallenge()
            }
        }

        removeTextFocus()
        scrollBottom()
    }

    private fun changeStep(step: ChallengeStep) {
        currentStep.value = step
        addSteps.value += step
    }

    private fun addChallenge() {
        val state = challengeAddState.value
        viewModelScope.launch {
            addChallengeUsecase(
                title = state.title,
                goalAmount = state.goalAmount,
                amount = state.amount.toLong(),
                count = state.count.toInt(),
                challengeType = state.challengeType,
                onSuccess = {
                    showSnackBar(MessageType.Message("챌린지가 추가되었습니다."))
                    complete()
                },
                onError = ::showSnackBar,
            )
        }
    }

    fun goalAmountValueChange(value: ValueState) {
        _challengeAddState.update {
            it.copy(
                goalAmount = value.value(it.goalAmountString).toLongOrNull() ?: 0,
            )
        }
    }

    fun challengeTypeSelected(challengeType: ChallengeType) {
        _challengeAddState.update {
            it.copy(challengeType = challengeType)
        }
    }

    fun amountValueChange(value: String) {
        _challengeAddState.update {
            it.copy(
                amount = value,
                count = calculatePaymentCount(it.goalAmount, value.toLongOrNull() ?: 0).toString(),
            )
        }
    }

    fun countValueChange(value: String) {
        _challengeAddState.update {
            it.copy(
                amount = calculatePaymentAmount(it.goalAmount, value.toIntOrNull() ?: 0).toString(),
                count = value,
            )
        }
    }

    fun titleChange(value: String) {
        _challengeAddState.update {
            it.copy(title = value)
        }
    }

    fun showNumberKeyboard() {
        removeTextFocus()
        _challengeModalEffect.update { ChallengeModalEffect.ShowNumberKeyboard }
    }

    private fun dismiss() {
        _challengeModalEffect.update { ChallengeModalEffect.Idle }
    }

    fun numberKeyboardDismiss() {
        nextStep()
        _challengeModalEffect.update { ChallengeModalEffect.Idle }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _challengeAddEffect.emit(ChallengeAddEffect.ShowSnackBar(messageType))
        }
    }

    private fun removeTextFocus() {
        viewModelScope.launch {
            _challengeAddEffect.emit(ChallengeAddEffect.RemoveTextFocus)
        }
    }

    private fun scrollBottom() {
        viewModelScope.launch {
            _challengeAddEffect.emit(ChallengeAddEffect.ScrollToBottom)
        }
    }

    private fun complete() {
        viewModelScope.launch {
            _challengeAddEffect.emit(ChallengeAddEffect.ChallengeAddComplete)
        }
    }

    private fun calculatePaymentCount(targetAmount: Long, paymentAmount: Long): Int {
        return ceil(targetAmount.toDouble() / paymentAmount.toDouble()).toInt()
    }

    private fun calculatePaymentAmount(targetAmount: Long, paymentCount: Int): Int {
        return ceil(targetAmount.toDouble() / paymentCount.toDouble()).toInt()
    }
}
