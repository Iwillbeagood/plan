package jun.money.mate.save.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.save.SavePlanList
import kotlin.math.ceil

@Stable
internal sealed interface SavingListState {

    @Immutable
    data object Loading : SavingListState

    @Immutable
    data class SavingListData(
        val savePlanList: SavePlanList,
    ) : SavingListState {

        val goldAcornCount: Int get() = (savePlanList.executedTotal / 1_000_000).toInt()
        val acornCount: Int get() = ceil((savePlanList.executedTotal % 1_000_000).toDouble() / 100_000).toInt()
    }
}
