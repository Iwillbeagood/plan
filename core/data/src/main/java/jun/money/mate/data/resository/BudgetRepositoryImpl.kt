package jun.money.mate.data.resository

import jun.money.mate.data.mapper.toBudget
import jun.money.mate.data_api.database.BudgetRepository
import jun.money.mate.database.dao.BudgetDao
import jun.money.mate.database.entity.BudgetEntity
import jun.money.mate.database.entity.BudgetWithUsed
import jun.money.mate.model.consumption.Budget
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
     private val budgetDao: BudgetDao
) : BudgetRepository {

    override suspend fun upsert(budget: Budget) {
        try {
            budgetDao.upsert(
                BudgetEntity(
                    id = budget.id,
                    title = budget.title,
                    budget = budget.budget,
                    amountUsed = budget.amountUsed,
                    pastBudgets = budget.pastBudgets,
                )
            )
        } catch (e: Exception) {
            Logger.e("upsert error: $e")
        }
    }

    override fun getBudgetsFlow(): Flow<List<Budget>> {
        return budgetDao.getListFlow().map { list ->
            list.map(BudgetWithUsed::toBudget)
        }.catch {
            Logger.e("getBudgetsFlow error: $it")
        }
    }

    override suspend fun getBudget(id: Long): Budget {
        return budgetDao.get(id).toBudget()
    }

    override suspend fun deleteBudget(id: Long) {
        try {
            budgetDao.delete(id)
        } catch (e: Exception) {
            Logger.e("deleteBudget error: $e")
        }
    }
}