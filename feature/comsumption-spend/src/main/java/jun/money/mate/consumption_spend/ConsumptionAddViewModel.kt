package jun.money.mate.consumption_spend

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.ConsumptionRepository
import jun.money.mate.domain.AddConsumptionUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.MainTabRoute
import jun.money.mate.navigation.argument.AddType
import jun.money.mate.navigation.utils.toRouteType
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
internal class ConsumptionAddViewModel @Inject constructor(
    private val addConsumptionUsecase: AddConsumptionUsecase,
    private val consumptionRepository: ConsumptionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val addType = savedStateHandle.toRouteType<MainTabRoute.ConsumptionSpend.Add, AddType>().addType

    private val _consumptionAddState = MutableStateFlow<ConsumptionAddState>(ConsumptionAddState.Loading)
    val consumptionAddState: StateFlow<ConsumptionAddState> = _consumptionAddState.onStart {
        init()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ConsumptionAddState.Loading
    )

    private val _consumptionModalEffect = MutableStateFlow<ConsumptionModalEffect>(ConsumptionModalEffect.Idle)
    val consumptionModalEffect: StateFlow<ConsumptionModalEffect> get() = _consumptionModalEffect

    private val _consumptionAddEffect = MutableSharedFlow<ConsumptionAddEffect>()
    val consumptionAddEffect: SharedFlow<ConsumptionAddEffect> get() = _consumptionAddEffect.asSharedFlow()

    private fun init() {
        viewModelScope.launch {
            _consumptionAddState.update {
                when (addType) {
                    AddType.New -> ConsumptionAddState.ConsumptionData(
                        id = System.currentTimeMillis(),
                        title = "",
                        amount = 0,
                        date = LocalDate.now(),
                        planTitle = ""
                    )

                    is AddType.Edit -> {
                        consumptionRepository.getConsumptionById(addType.id).let {
                            ConsumptionAddState.ConsumptionData(
                                id = it.id,
                                title = it.title,
                                amount = it.amount,
                                date = it.consumptionDate,
                                planTitle = it.planTitle
                            )
                        }
                    }
                }
            }
        }
    }

    fun addSpendingPlan() {
        val state = _consumptionAddState.value as? ConsumptionAddState.ConsumptionData ?: return
        viewModelScope.launch {
            addConsumptionUsecase(
                id = state.id,
                title = state.title,
                amount = state.amount,
                date = state.date,
                planTitle = state.planTitle,
                onSuccess = {
                    showSnackBar(
                        MessageType.Message(
                            "지출이 ${
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

    fun titleValueChange(value: String) {
        val state = _consumptionAddState.value as? ConsumptionAddState.ConsumptionData ?: return

        _consumptionAddState.update {
            state.copy(title = value)
        }
    }

    fun amountValueChange(value: String) {
        val state = _consumptionAddState.value as? ConsumptionAddState.ConsumptionData ?: return

        _consumptionAddState.update {
            state.copy(
                amount = value.toLongOrNull() ?: 0
            )
        }
    }


    fun dateSelected(date: LocalDate) {
        val state = _consumptionAddState.value as? ConsumptionAddState.ConsumptionData ?: return
        _consumptionAddState.update {
            state.copy(date = date)
        }
    }

    fun categorySelected(planTitle: String) {
        showDatePicker()

        val state = _consumptionAddState.value as? ConsumptionAddState.ConsumptionData ?: return

        _consumptionAddState.update {
            state.copy(planTitle = planTitle)
        }
    }

    fun showDatePicker() {
        val state = _consumptionAddState.value as? ConsumptionAddState.ConsumptionData ?: return

        _consumptionModalEffect.update { ConsumptionModalEffect.ShowDatePicker(state.date) }
    }

    fun showCategoryBottomSheet() {
        viewModelScope.launch {
        }
    }

    fun onDismiss() {
        _consumptionModalEffect.update { ConsumptionModalEffect.Idle }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _consumptionAddEffect.emit(ConsumptionAddEffect.ShowSnackBar(messageType))
        }
    }

    fun incomeAddComplete() {
        viewModelScope.launch {
            _consumptionAddEffect.emit(ConsumptionAddEffect.SpendingPlanAddComplete)
        }
    }
}

@Stable
internal sealed interface ConsumptionAddState {

    @Immutable
    data object Loading : ConsumptionAddState

    @Immutable
    data class ConsumptionData(
        val id: Long,
        val title: String,
        val amount: Long,
        val date: LocalDate,
        val planTitle: String,
    ) : ConsumptionAddState {

        val amountString get() = if (amount > 0) amount.toString() else ""
        val amountWon get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
    }
}

@Stable
internal sealed interface ConsumptionAddEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : ConsumptionAddEffect

    @Immutable
    data object SpendingPlanAddComplete : ConsumptionAddEffect
}

@Stable
internal sealed interface ConsumptionModalEffect {

    @Immutable
    data object Idle : ConsumptionModalEffect

    @Immutable
    data class ShowDatePicker(val date: LocalDate) : ConsumptionModalEffect

    @Immutable
    data class ShowCategoryBottomSheet(val consumptionPlanTitles: List<String>) : ConsumptionModalEffect
}