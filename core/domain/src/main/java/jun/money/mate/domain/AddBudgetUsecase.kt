package jun.money.mate.domain

import jun.money.mate.dataApi.database.BudgetRepository
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.etc.error.MessageType
import javax.inject.Inject

class AddBudgetUsecase @Inject constructor(
    private val budgetRepository: BudgetRepository,
) {

    suspend operator fun invoke(
        title: String,
        budget: Long,
        onSuccess: () -> Unit,
        onError: (MessageType) -> Unit,
    ) {
        if (title.isEmpty()) {
            onError(MessageType.Message("예산의 제목을 입력해 주세요"))
            return
        }

        if (budget <= 0) {
            onError(MessageType.Message("예산을 입력해 주세요"))
            return
        }

        val id = System.currentTimeMillis()
        budgetRepository.upsert(
            Budget(
                id = id,
                title = title,
                budget = budget,
                pastBudgets = emptyList(),
                usedList = emptyList(),
            ),
        )
        onSuccess()
    }
}
