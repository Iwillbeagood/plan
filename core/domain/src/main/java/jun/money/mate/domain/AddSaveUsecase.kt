package jun.money.mate.domain

import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SavingsType
import jun.money.mate.model.save.SavePlan
import java.time.LocalDate
import javax.inject.Inject

class AddSaveUsecase @Inject constructor(
    private val saveRepository: SaveRepository
) {

    suspend operator fun invoke(
        amount: Long,
        category: SavingsType?,
        planDay: Int,
        onSuccess: () -> Unit,
        onError: (MessageType) -> Unit
    ) {

        if (amount <= 0) {
            onError(MessageType.Message("저축 금액을 입력해 주세요"))
            return
        }

        saveRepository.upsertSavePlan(
            SavePlan(
                id = System.currentTimeMillis(),
                amount = amount,
                planDay = planDay,
                savingsType = category ?: SavingsType.보통예금,
                executeMonth = LocalDate.now().monthValue,
                executed = false,
                selected = false
            )
        )
        onSuccess()
    }
}