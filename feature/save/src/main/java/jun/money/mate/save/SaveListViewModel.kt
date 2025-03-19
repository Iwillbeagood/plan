package jun.money.mate.save

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.domain.GetChallengeProgressUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SavingChallenge
import jun.money.mate.save.contract.SavingListEffect
import jun.money.mate.save.contract.SavingListState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
internal class SavingListViewModel @Inject constructor(
    private val saveRepository: SaveRepository,
    getChallengeProgressUsecase: GetChallengeProgressUsecase
) : ViewModel() {

    private val _month = MutableStateFlow<YearMonth>(YearMonth.now())
    val month: StateFlow<YearMonth> get() = _month

    private val _savingListState = MutableStateFlow<SavingListState>(SavingListState.Loading)
    val savingListState: StateFlow<SavingListState> = _savingListState.onStart {
        loadSpending()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SavingListState.Loading
    )

    val challengeState: StateFlow<List<SavingChallenge>> = month.flatMapLatest { month ->
        getChallengeProgressUsecase(month)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _savingListEffect = MutableSharedFlow<SavingListEffect>()
    val savingListEffect: SharedFlow<SavingListEffect> get() = _savingListEffect.asSharedFlow()

    private fun loadSpending() {
        viewModelScope.launch {
            month.flatMapLatest { month ->
                saveRepository.getSavingFlow(month)
            }.collect { savePlan ->
                _savingListState.update {
                    SavingListState.SavingListData(savePlan)
                }
            }
        }
    }

    /**
     *  일단 기간이 정해져있는 save의 경우에는
     * */
    fun executeChange(executed: Boolean, id: Long) {
        viewModelScope.launch {
            saveRepository.updateExecuteState(id, executed)
        }
    }

    fun prevMonth() {
        _month.update {
            it.minusMonths(1)
        }
    }

    fun nextMonth() {
        _month.update {
            it.plusMonths(1)
        }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _savingListEffect.emit(SavingListEffect.ShowSnackBar(messageType))
        }
    }
}