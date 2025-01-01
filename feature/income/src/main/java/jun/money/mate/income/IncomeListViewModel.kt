package jun.money.mate.income

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.model.etc.ViewMode
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class IncomeListViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository
) : ViewModel() {

    var dateState = MutableStateFlow<LocalDate>(LocalDate.now())
        private set

    private val _incomeListState = MutableStateFlow<IncomeListState>(IncomeListState.Loading)
    val incomeListState: StateFlow<IncomeListState> = _incomeListState.onStart {
        loadIncomes()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IncomeListState.Loading
    )

    val incomeListViewMode: StateFlow<ViewMode> = incomeListState.flatMapLatest {
        flowOf(
            when (it) {
                is IncomeListState.UiData -> it.incomeListViewMode
                else -> ViewMode.LIST
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = ViewMode.LIST
    )

    private val _incomeListEffect = MutableSharedFlow<IncomeListEffect>()
    val incomeListEffect: SharedFlow<IncomeListEffect> get() = _incomeListEffect.asSharedFlow()

    private fun loadIncomes() {
        viewModelScope.launch {
            dateState.flatMapLatest {
                incomeRepository.getIncomesByMonth(it)
            }.collect {
                _incomeListState.value = if (it.incomes.isEmpty()) {
                    IncomeListState.Empty
                } else {
                    IncomeListState.UiData(it)
                }
            }
        }
    }

    fun changeIncomeSelected(income: Income) {
        val incomeState = incomeListState.value as? IncomeListState.UiData ?: return

        viewModelScope.launch {
            _incomeListState.update {
                incomeState.copy(
                    incomeList = incomeState.incomeList.copy(
                        incomes = incomeState.incomeList.incomes.map {
                            if (it.id == income.id) {
                                it.copy(selected = !it.selected)
                            } else {
                                it.copy(selected = false)
                            }
                        }
                    )
                )
            }
        }
    }

    fun editIncome() {
        val incomeState = incomeListState.value as? IncomeListState.UiData ?: return
        val selectedIncomeId = incomeState.selectedIncomeId ?: return

        viewModelScope.launch {
            _incomeListEffect.emit(IncomeListEffect.EditIncome(selectedIncomeId))
        }
    }

    fun deleteIncome() {
        val incomeState = incomeListState.value as? IncomeListState.UiData ?: return
        val selectedIncomeId = incomeState.selectedIncomeId ?: return

        viewModelScope.launch {
            incomeRepository.deleteById(selectedIncomeId)
            showSnackBar(MessageType.Message("수입이 삭제되었습니다"))
        }
    }

    fun onDateSelected(date: LocalDate) {
        dateState.update { date }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _incomeListEffect.emit(IncomeListEffect.ShowSnackBar(messageType))
        }
    }
}

@Stable
internal sealed interface IncomeListState {

    @Immutable
    data object Loading : IncomeListState

    @Immutable
    data object Empty : IncomeListState

    @Immutable
    data class UiData(
        val incomeList: IncomeList,
    ) : IncomeListState {

        val incomeListViewMode: ViewMode get() = if (incomeList.incomes.any { it.selected }) ViewMode.EDIT else ViewMode.LIST

        val selectedIncomeId get() = incomeList.incomes.firstOrNull { it.selected }?.id
    }
}

@Stable
internal sealed interface IncomeListEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : IncomeListEffect

    @Immutable
    data class EditIncome(val id: Long) : IncomeListEffect
}