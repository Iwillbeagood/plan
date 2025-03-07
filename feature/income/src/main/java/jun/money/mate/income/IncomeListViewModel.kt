package jun.money.mate.income

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.domain.DeleteIncomeUsecase
import jun.money.mate.income.contract.IncomeListEffect
import jun.money.mate.income.contract.IncomeListModalEffect
import jun.money.mate.income.contract.IncomeListState
import jun.money.mate.model.LeafOrder
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.Income
import jun.money.mate.model.income.IncomeList
import jun.money.mate.navigation.Route
import jun.money.mate.utils.flow.updateWithData
import jun.money.mate.utils.flow.withData
import kic.owner2.utils.etc.Logger
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
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
internal class IncomeListViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val deleteIncomeUsecase: DeleteIncomeUsecase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listData = savedStateHandle.toRoute<Route.Income.List>()
    val month: LocalDate = LocalDate.of(listData.year, listData.month, 1)

    private val _incomeListState = MutableStateFlow<IncomeListState>(IncomeListState.Loading)
    val incomeListState: StateFlow<IncomeListState> = _incomeListState.onStart {
        loadIncomes()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IncomeListState.Loading
    )

    private val _incomeListEffect = MutableSharedFlow<IncomeListEffect>()
    val incomeListEffect: SharedFlow<IncomeListEffect> get() = _incomeListEffect.asSharedFlow()

    private val _modalEffect = MutableStateFlow<IncomeListModalEffect>(IncomeListModalEffect.Hidden)
    val modalEffect: StateFlow<IncomeListModalEffect> get() = _modalEffect

    private val _leaves = MutableStateFlow<List<LeafOrder>>(emptyList())
    val leaves: StateFlow<List<LeafOrder>> get() = _leaves

    private fun loadIncomes() {
        viewModelScope.launch {
            incomeRepository.getIncomesByMonth(month)
                .collect { list ->
                    Logger.d("loadIncomes: $list")
                    _incomeListState.update {
                        makeLeaves(list)
                        IncomeListState.UiData(list)
                    }
                }
        }
    }

    private fun makeLeaves(list: IncomeList) {
        val count = ceil(list.monthlyTotal.toDouble() / 100000).toInt()
        val count2 = ceil(list.specificTotal.toDouble() / 100000).toInt()
        val list = List(count) {
            LeafOrder(false)
        }
        val list2 = List(count2) {
            LeafOrder(true)
        }

        _leaves.update {
            list + list2
        }
    }

    fun selectIncome(income: Income) {
        _incomeListState.updateWithData<IncomeListState, IncomeListState.UiData> {
            it.copy(
                incomeList = it.incomeList.copy(
                    incomes = it.incomeList.incomes.map { incomeItem ->
                        if (incomeItem.id == income.id) {
                            incomeItem.copy(isSelected = !incomeItem.isSelected)
                        } else {
                            incomeItem
                        }
                    }
                )
            )
        }
    }

    private fun unselectedIncomes() {
        _incomeListState.updateWithData<IncomeListState, IncomeListState.UiData> {
            it.copy(
                incomeList = it.incomeList.copy(
                    incomes = it.incomeList.incomes.map { incomeItem ->
                        incomeItem.copy(isSelected = false)
                    }
                )
            )
        }
    }

    fun deleteIncome() {
        viewModelScope.launch {
            incomeListState.withData<IncomeListState.UiData> {
                deleteIncomeUsecase(
                    incomes = it.selectedIncomes,
                    onSuccess = ::unselectedIncomes
                )
            }
        }
    }

    fun editIncome() {
        viewModelScope.launch {
            incomeListState.withData<IncomeListState.UiData> {
                val selectedIncomes = it.selectedIncomes
                if (selectedIncomes.size == 1) {
                    _incomeListEffect.emit(IncomeListEffect.EditIncome(selectedIncomes.first().id))
                    unselectedIncomes()
                }
            }
        }
    }

    fun showDeleteDialog() {
        viewModelScope.launch {
            _modalEffect.emit(IncomeListModalEffect.ShowDeleteConfirmDialog)
        }
    }

    fun hideModal() {
        viewModelScope.launch {
            _modalEffect.emit(IncomeListModalEffect.Hidden)
        }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _incomeListEffect.emit(IncomeListEffect.ShowSnackBar(messageType))
        }
    }
}





