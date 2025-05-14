package jun.money.mate.cost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.cost.component.CostCalendarValue
import jun.money.mate.cost.contract.CostEffect
import jun.money.mate.cost.contract.CostState
import jun.money.mate.cost.contract.CostModalEffect
import jun.money.mate.cost.navigation.Title
import jun.money.mate.dataApi.database.CostRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.Cost
import jun.money.mate.utils.flow.updateWithData
import jun.money.mate.utils.flow.withData
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
import javax.inject.Inject

@HiltViewModel
internal class CostViewModel @Inject constructor(
    private val costRepository: CostRepository
) : ViewModel() {

    private val _costState = MutableStateFlow<CostState>(CostState.Loading)
    val costState: StateFlow<CostState> = _costState
        .onStart {
            fetchCost()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CostState.Loading
        )

    private val _costEffect = MutableSharedFlow<CostEffect>()
    val costEffect: SharedFlow<CostEffect> get() = _costEffect.asSharedFlow()

    private val _costModalEffect = MutableStateFlow<CostModalEffect>(CostModalEffect.Hidden)
    val costModalEffect: StateFlow<CostModalEffect> get() = _costModalEffect

    private fun fetchCost() {
        viewModelScope.launch {
            costRepository
                .getCostFlow()
                .collect { costs ->
                    _costState.update {
                        CostState.Data(
                            costs = costs
                        )
                    }
                }
        }
    }

    fun selectCost(cost: Cost) {
        viewModelScope.launch {
            _costState.updateWithData<CostState, CostState.Data> { state ->
                state.copy(
                    costs = state.costs.map {
                        it.copy(
                            selected = if (it.id == cost.id) {
                                !it.selected
                            } else {
                                it.selected
                            }
                        )
                    }
                )
            }
        }
    }

    fun selectCalendarValue(costCalendarValue: CostCalendarValue?) {
        viewModelScope.launch {
            _costState.updateWithData<CostState, CostState.Data> { state ->
                state.copy(
                    selectedCalendarValue = costCalendarValue
                )
            }
        }
    }

    fun editCost() {
        viewModelScope.launch {
            _costState.withData<CostState.Data> { state ->
                if (state.selectedCount != 1) return@launch
                val selectedCost = state.costs.find { it.selected } ?: return@launch
                _costEffect.emit(CostEffect.NavigateToCostDetail(selectedCost.id))
            }
        }
    }

    fun deleteCost() {
        viewModelScope.launch {
            _costState.withData<CostState.Data> { state ->
                costRepository.deleteByIds(state.selectedCosts)
                hideModal()
                showSnackBar(MessageType.Message("${Title}이 삭제되었습니다"))
            }
        }
    }

    fun unselectCost() {
        viewModelScope.launch {
            _costState.updateWithData<CostState, CostState.Data> { state ->
                state.copy(
                    costs = state.costs.map {
                        it.copy(
                            selected = false
                        )
                    }
                )
            }
        }
    }

    fun showDeleteDialog() {
        viewModelScope.launch {
            _costModalEffect.emit(CostModalEffect.ShowDeleteDialog)
        }
    }

    fun hideModal() {
        viewModelScope.launch {
            _costModalEffect.emit(CostModalEffect.Hidden)
        }
    }

    private fun showSnackBar(message: MessageType) {
        viewModelScope.launch {
            _costEffect.emit(CostEffect.ShowSnackBar(message))
        }
    }
}
