package jun.money.mate.finance

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.domain.GetFinanceUsecase
import jun.money.mate.finance.contract.FinanceState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class FinanceViewModel @Inject constructor(
    getFinanceUsecase: GetFinanceUsecase,
) : ViewModel() {

    val financeState: StateFlow<FinanceState> = getFinanceUsecase().map {
        FinanceState.FinanceData(
            incomeList = it.incomeList,
            savePlanList = it.savePlanList,
            challengeList = it.challenge,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FinanceState.Loading,
    )

    private val _financeEffect = MutableSharedFlow<FinanceEffect>()
    val financeEffect: SharedFlow<FinanceEffect> get() = _financeEffect.asSharedFlow()
}

@Stable
internal sealed interface FinanceEffect
