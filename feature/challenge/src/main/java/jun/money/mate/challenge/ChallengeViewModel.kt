package jun.money.mate.challenge

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.Route
import jun.money.mate.challenge.contract.SavingListEffect
import jun.money.mate.challenge.contract.SavingListState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
internal class SavingListViewModel @Inject constructor(
    private val saveRepository: SaveRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listData = savedStateHandle.toRoute<Route.Save.List>()
    val month: YearMonth = YearMonth.of(listData.year, listData.month)

    private val _savingListState = MutableStateFlow<SavingListState>(SavingListState.Loading)
    val savingListState: StateFlow<SavingListState> = _savingListState.onStart {
        loadSpending()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SavingListState.Loading
    )

    private val _savingListEffect = MutableSharedFlow<SavingListEffect>()
    val savingListEffect: SharedFlow<SavingListEffect> get() = _savingListEffect.asSharedFlow()

    private fun loadSpending() {
        viewModelScope.launch {
            saveRepository.getSavingFlow(month).collect { savePlan ->
                _savingListState.value = SavingListState.SavingListData(savePlan)
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

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _savingListEffect.emit(SavingListEffect.ShowSnackBar(messageType))
        }
    }
}