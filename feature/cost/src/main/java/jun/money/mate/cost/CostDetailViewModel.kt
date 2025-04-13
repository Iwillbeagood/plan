package jun.money.mate.cost

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.cost.contract.CostDetailEffect
import jun.money.mate.cost.contract.CostDetailState
import jun.money.mate.data_api.database.CostRepository
import jun.money.mate.domain.UpsertCostUsecase
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.CostType
import jun.money.mate.navigation.MainTabRoute
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
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
internal class CostDetailViewModel @Inject constructor(
    private val upsertCostUsecase: UpsertCostUsecase,
    private val costRepository: CostRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id = savedStateHandle.toRoute<MainTabRoute.Cost.Detail>().id

    private val _costAddState = MutableStateFlow<CostDetailState>(CostDetailState.Loading)
    val costDetailState: StateFlow<CostDetailState> =_costAddState.onStart {
        fetchCost()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CostDetailState.Loading
    )

    private val _costEffect = MutableSharedFlow<CostDetailEffect>()
    val costEffect: SharedFlow<CostDetailEffect> get() = _costEffect.asSharedFlow()

    private fun fetchCost() {
        viewModelScope.launch {
            val cost = costRepository.getCostById(id)
            _costAddState.update {
                CostDetailState.UiData(
                    id = cost.id,
                    amount = cost.amount,
                    day = cost.day,
                    costType = cost.costType
                )
            }
        }
    }

    fun editCost() {
        viewModelScope.launch {
            costDetailState.withData<CostDetailState.UiData> {
                upsertCostUsecase(
                    id = it.id,
                    amount = it.amount,
                    day = it.day,
                    costType = it.costType,
                    onSuccess = ::editComplete,
                    onError = ::showSnackBar
                )
            }
        }
    }

    fun costTypeSelected(costType: CostType?) {
        _costAddState.updateWithData<CostDetailState, CostDetailState.UiData> {
            it.copy(costType = costType)
        }
    }

    fun amountValueChange(value: String) {
        _costAddState.updateWithData<CostDetailState, CostDetailState.UiData> {
            it.copy(
                amount = value.toLongOrNull() ?: 0
            )
        }
    }

    fun daySelected(day: String) {
        _costAddState.updateWithData<CostDetailState, CostDetailState.UiData> {
            it.copy(day = day.toInt())
        }
    }

    private fun editComplete() {
        viewModelScope.launch {
            _costEffect.emit(CostDetailEffect.EditComplete)
        }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _costEffect.emit(CostDetailEffect.ShowSnackBar(messageType))
        }
    }
}