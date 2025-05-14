package jun.money.mate.domain

import jun.money.mate.dataApi.database.SaveRepository
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavingsType
import javax.inject.Inject

class EditSaveUsecase @Inject constructor(
    private val saveRepository: SaveRepository,
) {

    suspend operator fun invoke(
        savePlan: SavePlan,
    ) {
        when (savePlan.savingsType) {
            is SavingsType.보험저축 -> editPeriodic(savePlan)
            is SavingsType.적금 -> editPeriodic(savePlan)
            else -> editBasic(savePlan)
        }
    }

    private suspend fun editPeriodic(savePlan: SavePlan) {
        saveRepository.upsertSavePlan(savePlan)
    }

    private suspend fun editBasic(savePlan: SavePlan) {
        val saveList = saveRepository.getSavingByParentId(savePlan.id)
        saveList.forEach {
            saveRepository.upsertSavePlan(
                it.copy(
                    amount = savePlan.amount,
                    day = savePlan.day,
                ),
            )
        }
    }
}
