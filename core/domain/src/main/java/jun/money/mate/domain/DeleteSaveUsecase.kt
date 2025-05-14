package jun.money.mate.domain

import jun.money.mate.dataApi.database.SaveRepository
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavingsType
import javax.inject.Inject

class DeleteSaveUsecase @Inject constructor(
    private val saveRepository: SaveRepository,
) {

    suspend operator fun invoke(
        savePlan: SavePlan,
    ) {
        when (savePlan.savingsType) {
            is SavingsType.보험저축 -> deletePeriodic(savePlan)
            is SavingsType.적금 -> deletePeriodic(savePlan)
            else -> deleteBasic(savePlan)
        }
    }

    private suspend fun deletePeriodic(savePlan: SavePlan) {
        saveRepository.deleteById(savePlan.id)
    }

    private suspend fun deleteBasic(savePlan: SavePlan) {
        val ids = saveRepository.getSavingByParentId(savePlan.id).map { it.id }
        saveRepository.deleteByIds(ids)
    }
}
