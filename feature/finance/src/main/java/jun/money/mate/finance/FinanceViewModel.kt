package jun.money.mate.finance

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.domain.GetFinanceUsecase
import jun.money.mate.model.income.IncomeList
import jun.money.mate.model.save.SavePlanList
import jun.money.mate.navigation.MainBottomNavItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
internal class FinanceViewModel @Inject constructor(
    getFinanceUsecase: GetFinanceUsecase
) : ViewModel() {

    private val _month = MutableStateFlow<YearMonth>(YearMonth.now())
    val month: StateFlow<YearMonth> get() = _month

    val financeState: StateFlow<FinanceState> = month.flatMapLatest {
        getFinanceUsecase(it)
    }.map {
        FinanceState.FinanceData(
            incomeList = it.incomeList,
            savePlanList = it.savePlanList,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FinanceState.Loading
    )

    private val _financeEffect = MutableSharedFlow<FinanceEffect>()
    val financeEffect: SharedFlow<FinanceEffect> get() = _financeEffect.asSharedFlow()

    fun prevMonth() {
        _month.update {
            it.minusMonths(1)
        }
    }

    fun nextMonth() {
        _month.update {
            it.plusMonths(1)
        }
    }
}

@Stable
internal sealed interface FinanceState {

    @Immutable
    data object Loading : FinanceState

    @Immutable
    data class FinanceData(
        val incomeList: IncomeList,
        val savePlanList: SavePlanList,
    ) : FinanceState
}

@Stable
internal sealed interface FinanceEffect {

}