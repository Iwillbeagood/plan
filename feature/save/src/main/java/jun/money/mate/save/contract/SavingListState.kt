package jun.money.mate.save.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.EditMode
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

        val editMode
            get() = savePlanList.savePlans.count { it.selected }.let {
                if (it > 1) {
                    EditMode.DELETE_ONLY
                } else if (it == 1) {
                    EditMode.EDIT
                } else {
                    EditMode.LIST
                }
            }

        val goldAcornCount: Int get() = (savePlanList.total / 1_000_000).toInt()
        val acornCount: Int get() = ceil((savePlanList.total % 1_000_000).toDouble() / 100_000).toInt()

        val selectedPlansId get() = savePlanList.savePlans.filter { it.selected }.map { it.id }
        val selectedId get() = savePlanList.savePlans.firstOrNull { it.selected }?.id
    }
}
