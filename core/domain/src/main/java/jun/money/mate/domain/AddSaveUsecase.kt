package jun.money.mate.domain

import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SaveCategory
import jun.money.mate.model.save.SavePlan
import java.time.LocalDate
import javax.inject.Inject

class AddSaveUsecase @Inject constructor(
    private val saveRepository: SaveRepository
) {

    suspend operator fun invoke(
        id: Long,
        title: String,
        amount: Long,
        category: SaveCategory?,
        planDay: Int,
        onSuccess: () -> Unit,
        onError: (MessageType) -> Unit
    ) {
        if (title.isEmpty()) {
            onError(MessageType.Message("저금명을 입력해 주세요"))
            return
        }

        if (amount <= 0) {
            onError(MessageType.Message("저금 금액을 입력해 주세요"))
            return
        }

        if (category == null) {
            onError(MessageType.Message("저금 카테고리를 선택해 주세요"))
            return
        }

        saveRepository.upsertSavePlan(
            SavePlan(
                id = id,
                title = title,
                amount = amount,
                planDay = planDay,
                saveCategory = category,
                executeMonth = LocalDate.now().monthValue,
                executed = false,
                selected = false
            )
        )
        onSuccess()
    }
}