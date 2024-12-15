package jun.money.mate.save

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.domain.AddSaveUsecase
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SaveCategory
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
internal class SaveAddViewModel @Inject constructor(
    private val addSaveUsecase: AddSaveUsecase,
    private val saveRepository: SaveRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val addType = savedStateHandle.toRouteType<MainTabRoute.Income.Add, AddType>().addType

    private val _saveAddState = MutableStateFlow<SaveAddState>(SaveAddState.Loading)
    val saveAddState: StateFlow<SaveAddState> = _saveAddState.onStart {
        init()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SaveAddState.Loading
    )

    private val _saveModalEffect = MutableStateFlow<SaveModalEffect>(SaveModalEffect.Idle)
    val saveModalEffect: StateFlow<SaveModalEffect> get() = _saveModalEffect

    private val _saveAddEffect = MutableSharedFlow<SaveAddEffect>()
    val saveAddEffect: SharedFlow<SaveAddEffect> get() = _saveAddEffect.asSharedFlow()

    private fun init() {
        viewModelScope.launch {
            _saveAddState.update {
                when (addType) {
                    AddType.New -> SaveAddState.SaveData(
                        id = System.currentTimeMillis(),
                        title = "",
                        amount = 0,
                        day = LocalDate.now().dayOfMonth,
                        category = null
                    )

                    is AddType.Edit -> {
                        saveRepository.getSavePlan(addType.id).let {
                            SaveAddState.SaveData(
                                id = it.id,
                                title = it.title,
                                amount = it.amount,
                                day = it.planDay,
                                category = it.saveCategory
                            )
                        }
                    }
                }
            }
        }
    }

    fun onAddSave() {
        val state = _saveAddState.value as? SaveAddState.SaveData ?: return
        viewModelScope.launch {
            addSaveUsecase(
                id = state.id,
                title = state.title,
                amount = state.amount,
                planDay = state.day,
                category = state.category,
                onSuccess = {
                    showSnackBar(
                        MessageType.Message(
                            "저금 계획이 ${
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

    fun onTitleValueChange(value: String) {
        val state = _saveAddState.value as? SaveAddState.SaveData ?: return

        _saveAddState.update {
            state.copy(title = value)
        }
    }

    fun onAmountValueChange(value: String) {
        val state = _saveAddState.value as? SaveAddState.SaveData ?: return

        _saveAddState.update {
            state.copy(
                amount = value.toLongOrNull() ?: 0
            )
        }
    }

    fun onDateSelected(date: LocalDate) {
        val state = _saveAddState.value as? SaveAddState.SaveData ?: return
        _saveAddState.update {
            state.copy(day = date.dayOfMonth)
        }

        onAddSave()
    }

    fun showDatePicker() {
        val state = _saveAddState.value as? SaveAddState.SaveData ?: return

        _saveModalEffect.update { SaveModalEffect.ShowDatePicker(LocalDate.now().withDayOfMonth(state.day)) }
    }

    fun categorySelected(category: SaveCategory) {
        val state = _saveAddState.value as? SaveAddState.SaveData ?: return
        _saveAddState.update { state.copy(category = category) }

        showDatePicker()
    }

    fun showCategoryBottomSheet() {
        _saveModalEffect.update { SaveModalEffect.ShowCategoryBottomSheet }
    }

    fun onDismiss() {
        _saveModalEffect.update { SaveModalEffect.Idle }
    }

    fun scrollToBottom() {
        viewModelScope.launch {
            _saveAddEffect.emit(SaveAddEffect.ScrollToBottom)
        }
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

@Stable
internal sealed interface SaveAddState {

    @Immutable
    data object Loading : SaveAddState

    @Immutable
    data class SaveData(
        val id: Long,
        val title: String,
        val amount: Long,
        val day: Int,
        val category: SaveCategory?
    ) : SaveAddState {

        val amountString get() = if (amount > 0) amount.toString() else ""
        val amountWon get() = if (amount > 0) CurrencyFormatter.formatAmountWon(amount) else ""
    }
}

@Stable
internal sealed interface SaveAddEffect {

    @Immutable
    data class ShowSnackBar(val messageType: MessageType) : SaveAddEffect

    @Immutable
    data object SaveAddComplete : SaveAddEffect

    @Immutable
    data object ScrollToBottom : SaveAddEffect
}

@Stable
internal sealed interface SaveModalEffect {

    @Immutable
    data object Idle : SaveModalEffect

    @Immutable
    data class ShowDatePicker(val date: LocalDate) : SaveModalEffect

    @Immutable
    data object ShowCategoryBottomSheet : SaveModalEffect
}