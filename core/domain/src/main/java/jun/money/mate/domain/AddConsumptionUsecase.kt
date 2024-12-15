package jun.money.mate.domain

import jun.money.mate.data_api.database.ConsumptionRepository
import jun.money.mate.model.consumption.Consumption
import jun.money.mate.model.etc.error.MessageType
import java.time.LocalDate
import javax.inject.Inject

class AddConsumptionUsecase @Inject constructor(
    private val consumptionRepository: ConsumptionRepository
) {

    suspend operator fun invoke(
        id: Long,
        title: String,
        amount: Long,
        date: LocalDate,
        planTitle: String,
        onSuccess: () -> Unit,
        onError: (MessageType) -> Unit
    ) {
        if (planTitle.isEmpty()) {
            onError(MessageType.Message("계획한 지출을 선택해 주세요"))
            return
        }

        if (title.isEmpty()) {
            onError(MessageType.Message("소비명을 입력해 주세요"))
            return
        }

        if (amount <= 0) {
            onError(MessageType.Message("소비액을 입력해 주세요"))
            return
        }

        consumptionRepository.upsertConsumption(
            consumption = Consumption(
                id = id,
                title = title,
                amount = amount,
                consumptionDate = date,
                planTitle = planTitle
            )
        )
        onSuccess()
    }
}