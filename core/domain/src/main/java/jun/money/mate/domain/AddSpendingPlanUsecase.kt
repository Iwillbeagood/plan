package jun.money.mate.domain

import jun.money.mate.data_api.database.SpendingPlanRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.SpendingCategory
import jun.money.mate.model.spending.SpendingCategory.Companion.name
import jun.money.mate.model.spending.SpendingPlan
import jun.money.mate.model.spending.SpendingType
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
        day: Int,
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
            spendingPlan = SpendingPlan(
                id = id,
                title = title,
                amount = amount,
                type = type,
                spendingCategory = category,
                planDay = day,
                isApply = true,
            ),
            onError = onError,
            onSuccess = onSuccess
        )
    }
}