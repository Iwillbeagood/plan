package jun.money.mate.income.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import jun.money.mate.model.etc.EditMode
import jun.money.mate.model.income.IncomeList

@Stable
internal sealed interface IncomeListState {

    @Immutable
    data object Loading : IncomeListState

    @Immutable
    data class UiData(val incomeList: IncomeList) : IncomeListState {

        val selectedIncomes get() = incomeList.incomes.filter { it.isSelected }

        val editMode get() = incomeList.incomes.count { it.isSelected }.let {
            if (it > 1) {
                EditMode.DELETE_ONLY
            } else if (it == 1) {
                EditMode.EDIT
            } else {
                EditMode.LIST
            }
        }
    }
}
