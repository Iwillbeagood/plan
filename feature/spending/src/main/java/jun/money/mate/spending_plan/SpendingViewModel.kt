package jun.money.mate.spending_plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.data_api.database.CostRepository
import jun.money.mate.model.Utils
import jun.money.mate.model.spending.Cost
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class SpendingViewModel @Inject constructor(
    private val costRepository: CostRepository
) : ViewModel() {

    val spendingState: StateFlow<SpendingState> = costRepository.getCostFlow()
        .map {
            SpendingState.Data(it)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            SpendingState.Loading
        )
}

internal sealed interface SpendingState {

    data object Loading : SpendingState

    data class Data(
        val costs: List<Cost>
    ) : SpendingState {

        val totalCostString get() = Utils.formatAmountWon(costs.sumOf { it.amount })
    }
}