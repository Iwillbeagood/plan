package jun.money.mate.consumption_spend

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.ConsumptionRepository
import jun.money.mate.domain.GetConsumptionFilterUsecase
import jun.money.mate.model.consumption.Consumption
import jun.money.mate.model.consumption.ConsumptionFilter
import jun.money.mate.model.consumption.ConsumptionFilter.Companion.selectedFilter
import jun.money.mate.model.consumption.ConsumptionFilter.Companion.toStringList
import jun.money.mate.model.consumption.ConsumptionList
import jun.money.mate.model.etc.ViewMode
import jun.money.mate.model.etc.error.MessageType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class ConsumptionListViewModel @Inject constructor(
    private val consumptionRepository: ConsumptionRepository,
    private val getConsumptionFilterUsecase: GetConsumptionFilterUsecase
) : ViewModel() {

    var dateState = MutableStateFlow<LocalDate>(LocalDate.now())
        private set

    private val _consumptionListState = MutableStateFlow<ConsumptionListState>(ConsumptionListState.Loading)
    val consumptionListState: StateFlow<ConsumptionListState> = _consumptionListState.onStart {
        loadSpending()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = ConsumptionListState.Loading
    )

    val consumptionListViewMode: StateFlow<ViewMode> = consumptionListState.flatMapLatest {
        flowOf(
            when (it) {
                is ConsumptionListState.ConsumptionListData -> it.consumptionListViewMode
                else -> ViewMode.LIST
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = ViewMode.LIST
    )

    private val _consumptionFilter = MutableStateFlow<List<ConsumptionFilter>>(emptyList())
    val consumptionFilter: StateFlow<List<ConsumptionFilter>> = _consumptionFilter.onStart {
        _consumptionFilter.update {
            getConsumptionFilterUsecase()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    private val _consumptionModalEffect =
        MutableStateFlow<ConsumptionDialogEffect>(ConsumptionDialogEffect.Idle)
    val consumptionModalEffect: StateFlow<ConsumptionDialogEffect> get() = _consumptionModalEffect

    private val _consumptionListEffect = MutableSharedFlow<ConsumptionListEffect>()
    val consumptionListEffect: SharedFlow<ConsumptionListEffect> get() = _consumptionListEffect.asSharedFlow()

    private fun loadSpending() {
        viewModelScope.launch {
            combine(
                dateState,
                consumptionFilter
            ) { date, filter ->
                date to filter.selectedFilter()
            }.flatMapLatest { (date, filter) ->
                consumptionRepository.getConsumptionByMonth(date).map { consumptionList ->
                    consumptionList.consumptions.filter {
                        filter.planTitle == ConsumptionFilter.ALL_TITLE || it.planTitle == filter.planTitle
                    }
                }
            }.collect {
                _consumptionListState.value = ConsumptionListState.ConsumptionListData(ConsumptionList(it))
            }
        }
    }

    fun changeConsumptionSelected(consumption: Consumption) {
        val state =
            consumptionListState.value as? ConsumptionListState.ConsumptionListData ?: return

        viewModelScope.launch {
            _consumptionListState.update {
                state.copy(
                    consumptionList = state.consumptionList.copy(
                        consumptions = state.consumptionList.consumptions.map {
                            if (it.id == consumption.id) {
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

    fun showFilterBottomSheet() {
        val filterValue = consumptionFilter.value

        viewModelScope.launch {
            _consumptionModalEffect.update {
                ConsumptionDialogEffect.ShowFilterBottomSheet(
                    filterValue.toStringList()
                )
            }
        }
    }

    fun showSpendingPlanDialog() {
        viewModelScope.launch {
            _consumptionModalEffect.update { ConsumptionDialogEffect.ShowSpendingPlanDialog }
        }
    }

    fun filterClicked(filterTitle: String) {
        _consumptionFilter.update {
            it.map {
                it.copy(
                    selected = it.planTitle == filterTitle
                )
            }
        }
        onDismissModal()
    }

    fun editSpending() {
        val state = consumptionListState.value as? ConsumptionListState.ConsumptionListData ?: return
        val id = state.selectedId ?: return

        viewModelScope.launch {
            _consumptionListEffect.emit(ConsumptionListEffect.EditSpendingPlan(id))
        }
    }

    fun deleteSpending() {
        val state = consumptionListState.value as? ConsumptionListState.ConsumptionListData ?: return
        val selectedIncomeId = state.selectedId ?: return

        viewModelScope.launch {
            consumptionRepository.deleteById(selectedIncomeId)
            showSnackBar(MessageType.Message("수입이 삭제되었습니다"))
        }
    }

    fun dateSelected(date: LocalDate) {
        dateState.update { date }
    }

    fun onDismissModal() {
        _consumptionModalEffect.update { ConsumptionDialogEffect.Idle }
    }

    fun showConsumptionAdd() {
        if (consumptionFilter.value.size < 2) {
            showSpendingPlanDialog()
            return
        }

        viewModelScope.launch {
            _consumptionListEffect.emit(ConsumptionListEffect.ShowConsumptionAdd)
        }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _consumptionListEffect.emit(ConsumptionListEffect.ShowSnackBar(messageType))
        }
    }
}

@Stable
internal sealed interface ConsumptionListState {

    @Immutable
    data object Loading : ConsumptionListState

    @Immutable
    data class ConsumptionListData(
        val consumptionList: ConsumptionList,
        val spendingTypeTabIndex: Int = 0,
    ) : ConsumptionListState {

        val consumptionListViewMode: ViewMode get() = if (consumptionList.consumptions.any { it.selected }) ViewMode.EDIT else ViewMode.LIST

        val selectedId get() = consumptionList.consumptions.firstOrNull { it.selected }?.id
    }
}

@Stable
internal sealed interface ConsumptionListEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : ConsumptionListEffect

    @Immutable
    data class EditSpendingPlan(val id: Long) : ConsumptionListEffect

    @Immutable
    data object ShowConsumptionAdd : ConsumptionListEffect
}

@Stable
internal sealed interface ConsumptionDialogEffect {

    @Immutable
    data object Idle : ConsumptionDialogEffect

    @Immutable
    data class ShowFilterBottomSheet(val filterTitles: List<String>) : ConsumptionDialogEffect

    @Immutable
    data object ShowSpendingPlanDialog : ConsumptionDialogEffect
}