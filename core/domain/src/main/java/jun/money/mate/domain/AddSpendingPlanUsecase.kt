package jun.money.mate.domain

import jun.money.mate.data_api.database.SpendingPlanRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingCategory.Companion.name
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingType
import java.time.LocalDate
import javax.inject.Inject

class AddSpendingPlanUsecase @Inject constructor(
    private val spendingPlanRepository: SpendingPlanRepository
) {

    suspend operator fun invoke(
        id: Long,
        title: String,
        amount: Long,
        type: SpendingType,
        category: SpendingCategory,
        date: LocalDate,
        onSuccess: () -> Unit,
        onError: (MessageType) -> Unit
    ) {
        if (title.isEmpty()) {
            onError(MessageType.Message("지출명을 입력해 주세요"))
            return
        }

        if (amount <= 0) {
            onError(MessageType.Message("지출액을 입력해 주세요"))
            return
        }

        spendingPlanRepository.upsertSpendingPlan(
            SpendingPlan(
                id = id,
                title = title,
                amount = amount,
                type = type,
                spendingCategoryName = category.name(),
                planDate = date,
                isApply = true
            )
        )
        onSuccess()
    }
}