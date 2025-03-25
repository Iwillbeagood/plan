package jun.money.mate.cost.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.save.Challenge

@Stable
internal sealed interface CostDetailState {

    @Immutable
    data object Loading : CostDetailState

    @Immutable
    data class CostDetailData(
        val challenge: Challenge,
    ) : CostDetailState {


    }
}
