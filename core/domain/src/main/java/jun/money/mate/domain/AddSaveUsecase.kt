package jun.money.mate.domain

import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.SaveCategory
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SaveType
import java.time.LocalDate
import javax.inject.Inject

class AddSaveUsecase @Inject constructor(
    private val saveRepository: SaveRepository
) {

    suspend operator fun invoke(
        id: Long,
        title: String,
        saveType: SaveType,
        amount: Long,
        amountGoal: Long,
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

        when (saveType) {
            SaveType.PlaningSave -> {
                if (amountGoal <= 0) {
                    onError(MessageType.Message("목표 금액을 입력해 주세요"))
                    return
                }
            }
            SaveType.ContinueSave -> {
                if (category == null) {
                    onError(MessageType.Message("저금 카테고리를 선택해 주세요"))
                    return
                }
            }
        }

        saveRepository.upsertSavePlan(
            SavePlan(
                id = id,
                title = title,
                saveType = saveType,
                amount = amount,
                amountGoal = amountGoal,
                planDay = planDay,
                saveCategory = category ?: SaveCategory.기타,
                executeMonth = LocalDate.now().monthValue,
                executed = false,
                selected = false
            )
        )
        onSuccess()
    }
}