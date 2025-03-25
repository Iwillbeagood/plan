package jun.money.mate.cost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.cost.component.CostCalendarValue
import jun.money.mate.cost.contract.CostState
import jun.money.mate.data_api.database.CostRepository
import jun.money.mate.model.Utils
import jun.money.mate.model.etc.DateType.Companion.date
import jun.money.mate.model.spending.Cost
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class CostViewModel @Inject constructor(
    costRepository: CostRepository
) : ViewModel() {

    val costState: StateFlow<CostState> = costRepository.getCostFlow()
        .map {
            CostState.Data(it)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            CostState.Loading
        )
}
