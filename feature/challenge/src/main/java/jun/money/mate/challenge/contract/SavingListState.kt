package jun.money.mate.challenge.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.save.SavePlanList

@Stable
internal sealed interface SavingListState {

    @Immutable
    data object Loading : SavingListState

    @Immutable
    data class SavingListData(
        val savePlanList: SavePlanList,
    ) : SavingListState {

        val goldAcornCount: Int get() = (savePlanList.executedTotal / 1_000_000).toInt()
        val acornCount: Int get() = (savePlanList.executedTotal / 100_000).toInt()
    }
}
