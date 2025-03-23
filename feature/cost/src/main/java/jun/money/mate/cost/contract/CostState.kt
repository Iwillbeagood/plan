package jun.money.mate.challenge.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.save.Challenge

@Stable
internal sealed interface CostState {

    @Immutable
    data object Loading : CostState

    @Immutable
    data class CostData(
        val challenge: Challenge,
    ) : CostState {


    }
}
