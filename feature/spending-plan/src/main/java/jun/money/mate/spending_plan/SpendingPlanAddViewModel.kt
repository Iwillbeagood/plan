package jun.money.mate.spending_plan

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.SpendingPlanRepository
import jun.money.mate.domain.AddSpendingPlanUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingCategoryType
import jun.money.mate.model.spending.SpendingType
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
internal class SpendingPlanAddViewModel @Inject constructor(
    private val addSpendingPlanUsecase: AddSpendingPlanUsecase,
    private val spendingPlanRepository: SpendingPlanRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val addType = savedStateHandle.toRouteType<MainTabRoute.SpendingPlan.Add, AddType>().addType

    private val _spendingPlanAddState = MutableStateFlow<SpendingPlanAddState>(SpendingPlanAddState.Loading)
    val spendingPlanAddState: StateFlow<SpendingPlanAddState> = _spendingPlanAddState.onStart {
        init()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SpendingPlanAddState.Loading
    )

    private val _spendingPlanModalEffect = MutableStateFlow<SpendingPlanModalEffect>(SpendingPlanModalEffect.Idle)
    val spendingPlanModalEffect: StateFlow<SpendingPlanModalEffect> get() = _spendingPlanModalEffect

    private val _spendingPlanAddEffect = MutableSharedFlow<SpendingPlanAddEffect>()
    val spendingPlanAddEffect: SharedFlow<SpendingPlanAddEffect> get() = _spendingPlanAddEffect.asSharedFlow()

    private fun init() {
        viewModelScope.launch {
            _spendingPlanAddState.update {
                when (addType) {
                    AddType.New -> SpendingPlanAddState.SpendingPlanData(
                        id = System.currentTimeMillis(),
                        title = "",
                        amount = 0,
                        date = LocalDate.now(),
                        type = SpendingType.ALL,
                        spendingCategory = SpendingCategory.NotSelected
                    )

                    is AddType.Edit -> {
                        spendingPlanRepository.getSpendingPlanById(addType.id).let {
                            SpendingPlanAddState.SpendingPlanData(
                                id = it.id,
                                title = it.title,
                                amount = it.amount,
                                date = it.planDate,
                                type = it.type,
                                spendingCategory = it.spendingCategory
                            )
                        }
                    }
                }
            }
        }
    }

    fun addSpendingPlan() {
        val state = _spendingPlanAddState.value as? SpendingPlanAddState.SpendingPlanData ?: return
        viewModelScope.launch {
            addSpendingPlanUsecase(
                id = state.id,
                title = state.title,
                amount = state.amount,
                type = state.type,
                category = state.spendingCategory,
                date = state.date,
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
                    spendingPlanAddComplete()
                },
                onError = ::showSnackBar
            )
        }
    }

    fun titleValueChange(value: String) {
        val state = _spendingPlanAddState.value as? SpendingPlanAddState.SpendingPlanData ?: return

        _spendingPlanAddState.update {
            state.copy(title = value)
        }
    }

    fun amountValueChange(value: String) {
        val state = _spendingPlanAddState.value as? SpendingPlanAddState.SpendingPlanData ?: return

        _spendingPlanAddState.update {
            state.copy(
                amount = value.toLongOrNull() ?: 0
            )
        }
    }

    fun applyTypeSelected(type: SpendingType) {
        val state = _spendingPlanAddState.value as? SpendingPlanAddState.SpendingPlanData ?: return

        _spendingPlanAddState.update {
            state.copy(type = type)
        }
    }

    fun dateSelected(date: LocalDate) {
        val state = _spendingPlanAddState.value as? SpendingPlanAddState.SpendingPlanData ?: return
        _spendingPlanAddState.update {
            state.copy(date = date)
        }

        addSpendingPlan()
    }

    fun categorySelected(category: SpendingCategoryType) {
        showDatePicker()

        val state = _spendingPlanAddState.value as? SpendingPlanAddState.SpendingPlanData ?: return

        _spendingPlanAddState.update {
            state.copy(spendingCategory = SpendingCategory.CategoryType(category))
        }
    }

    fun showDatePicker() {
        val state = _spendingPlanAddState.value as? SpendingPlanAddState.SpendingPlanData ?: return

        _spendingPlanModalEffect.update { SpendingPlanModalEffect.ShowDatePicker(state.date) }
    }

    fun showCategoryBottomSheet() {
        val state = _spendingPlanAddState.value as? SpendingPlanAddState.SpendingPlanData ?: return

        _spendingPlanModalEffect.update { SpendingPlanModalEffect.ShowCategoryBottomSheet(state.spendingCategory) }
    }

    fun onDismiss() {
        _spendingPlanModalEffect.update { SpendingPlanModalEffect.Idle }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _spendingPlanAddEffect.emit(SpendingPlanAddEffect.ShowSnackBar(messageType))
        }
    }

    private fun spendingPlanAddComplete() {
        viewModelScope.launch {
            _spendingPlanAddEffect.emit(SpendingPlanAddEffect.SpendingPlanAddComplete)
        }
    }
}

@Stable
internal sealed interface SpendingPlanAddState {

    @Immutable
    data object Loading : SpendingPlanAddState

    @Immutable
    data class SpendingPlanData(
        val id: Long,
        val title: String,
        val amount: Long,
        val date: LocalDate,
        val type: SpendingType,
        val spendingCategory: SpendingCategory,
    ) : SpendingPlanAddState {

        val amountString get() = if (amount > 0) amount.toString() else ""
        val amountWon get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
    }
}

@Stable
internal sealed interface SpendingPlanAddEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : SpendingPlanAddEffect

    @Immutable
    data object SpendingPlanAddComplete : SpendingPlanAddEffect
}

@Stable
internal sealed interface SpendingPlanModalEffect {

    @Immutable
    data object Idle : SpendingPlanModalEffect

    @Immutable
    data class ShowDatePicker(val date: LocalDate) : SpendingPlanModalEffect

    @Immutable
    data class ShowCategoryBottomSheet(val spendingCategory: SpendingCategory) : SpendingPlanModalEffect
}