package jun.money.mate.domain

import jun.money.mate.data_api.database.CostRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.spending.Cost
import jun.money.mate.model.spending.CostType
import javax.inject.Inject

class UpsertCostUsecase @Inject constructor(
    private val costRepository: CostRepository
) {

    suspend operator fun invoke(
        id: Long = System.currentTimeMillis(),
        amount: Long,
        costType: CostType?,
        day: Int,
        onSuccess: () -> Unit,
        onError: (MessageType) -> Unit
    ) {
        if (costType == null) {
            onError(MessageType.Message("고정 지출 유형을 선택해 주세요"))
            return
        }

        if (amount <= 0) {
            onError(MessageType.Message("고정 지출 금액을 입력해 주세요"))
            return
        }

        if (day == 0) {
            onError(MessageType.Message("날짜를 선택해 주세요"))
            return
        }

        costRepository.upsertCost(
            cost = Cost(
                id = id,
                amount = amount,
                costType = costType,
                day = day
            )
        )
        onSuccess()
    }
}