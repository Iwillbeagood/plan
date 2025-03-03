package jun.money.mate.income

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.domain.EditIncomeUsecase
import jun.money.mate.income.contract.EditState
import jun.money.mate.income.contract.IncomeEffect
import jun.money.mate.income.contract.IncomeModalEffect
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.Route
import jun.money.mate.ui.number.ValueState
import jun.money.mate.ui.number.ValueState.Companion.value
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
import javax.inject.Inject

@HiltViewModel
internal class IncomeEditViewModel @Inject constructor(
    private val editIncomeUsecase: EditIncomeUsecase,
    private val incomeRepository: IncomeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val originId = savedStateHandle.toRoute<Route.Income.Edit>().id

    private val _editState = MutableStateFlow<EditState>(EditState.Loading)
    val editState: StateFlow<EditState> = _editState.onStart {
        init()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = EditState.Loading
    )

    private val _incomeModalEffect = MutableStateFlow<IncomeModalEffect>(IncomeModalEffect.Idle)
    val incomeModalEffect: StateFlow<IncomeModalEffect> get() = _incomeModalEffect

    private val _incomeEffect = MutableSharedFlow<IncomeEffect>()
    val incomeEffect: SharedFlow<IncomeEffect> get() = _incomeEffect.asSharedFlow()

    private fun init() {
        viewModelScope.launch {
            _editState.update {
                incomeRepository.getIncomeById(originId).let {
                    EditState.UiData(
                        id = it.id,
                        title = it.title,
                        amount = it.amount,
                        dateType = it.dateType,
                        originIncome = it
                    )
                }
            }
        }
    }

    fun editIncome() {
        editState.withData<EditState.UiData> {
            viewModelScope.launch {
                editIncomeUsecase(
                    id = it.id,
                    title = it.title,
                    amount = it.amount,
                    dateType = it.dateType,
                    originIncome = it.originIncome,
                    onSuccess = {
                        showSnackBar(
                            MessageType.Message("수입이 수정 되었습니다.")
                        )
                        incomeAddComplete()
                    },
                    onError = ::showSnackBar
                )
            }
        }
    }

    fun titleValueChange(value: String) {
        _editState.updateWithData<EditState, EditState.UiData> {
            it.copy(title = value)
        }
    }

    fun amountValueChange(value: ValueState) {
        _editState.updateWithData<EditState, EditState.UiData> {
            it.copy(
                amount = value.value(it.amountString).toLongOrNull() ?: 0
            )
        }
    }

    fun dateSelected(date: LocalDate) {
        _editState.updateWithData<EditState, EditState.UiData> {
            it.copy(dateType = DateType.Specific(date))
        }

        dismiss()
    }

    fun daySelected(day: String) {
        _editState.updateWithData<EditState, EditState.UiData> {
            it.copy(dateType = DateType.Monthly(day.toInt()))
        }

        dismiss()
    }

    fun showNumberKeyboard() {
        _incomeModalEffect.update { IncomeModalEffect.ShowNumberKeyboard }
    }

    fun dismiss() {
        _incomeModalEffect.update { IncomeModalEffect.Idle }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _incomeEffect.emit(IncomeEffect.ShowSnackBar(messageType))
        }
    }

    private fun incomeAddComplete() {
        viewModelScope.launch {
            _incomeEffect.emit(IncomeEffect.IncomeComplete)
        }
    }
}


