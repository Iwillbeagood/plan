package jun.money.mate.data.resository

import jun.money.mate.data.mapper.toBudget
import jun.money.mate.dataApi.database.BudgetRepository
import jun.money.mate.database.dao.BudgetDao
import jun.money.mate.database.entity.BudgetEntity
import jun.money.mate.database.entity.BudgetWithUsed
import jun.money.mate.database.entity.PastBudgetEntity
import jun.money.mate.database.entity.UsedEntity
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.consumption.PastBudget
import jun.money.mate.model.consumption.Used
import jun.money.mate.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
) : BudgetRepository {

    override suspend fun upsert(budget: Budget) {
        try {
            budgetDao.upsert(
                BudgetEntity(
                    id = budget.id,
                    title = budget.title,
                    budget = budget.budget,
                ),
            )
        } catch (e: Exception) {
            Logger.e("upsert error: $e")
        }
    }

    override suspend fun insertUsed(used: Used) {
        try {
            budgetDao.insertUsed(
                UsedEntity(
                    title = used.meno,
                    budgetId = used.budgetId,
                    date = used.date,
                    amount = used.amount,
                ),
            )
        } catch (e: Exception) {
            Logger.e("insertUsed error: $e")
        }
    }

    override suspend fun insertPastBudget(pastBudget: PastBudget, budgetId: Long) {
        try {
            budgetDao.insertPastBudget(
                PastBudgetEntity(
                    budgetId = budgetId,
                    budget = pastBudget.budget,
                    amountUsed = pastBudget.amountUsed,
                    date = pastBudget.date,
                ),
            )
        } catch (e: Exception) {
            Logger.e("insertPastBudget error: $e")
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

    override fun getBudgetFlow(id: Long): Flow<Budget> {
        return budgetDao.getFlow(id).map(BudgetWithUsed::toBudget)
            .catch {
                Logger.e("getBudgetFlow error: $it")
            }
    }

    override suspend fun updateUsed(used: Used) {
        try {
            budgetDao.updateUsed(
                UsedEntity(
                    id = used.id,
                    title = used.meno,
                    budgetId = used.budgetId,
                    date = used.date,
                    amount = used.amount,
                ),
            )
        } catch (e: Exception) {
            Logger.e("updateUsed error: $e")
        }
    }

    override suspend fun deleteBudget(id: Long) {
        try {
            budgetDao.delete(id)
        } catch (e: Exception) {
            Logger.e("deleteBudget error: $e")
        }
    }

    override suspend fun deleteUsed(vararg id: Long) {
        try {
            id.forEach {
                budgetDao.deleteUsed(it)
            }
        } catch (e: Exception) {
            Logger.e("deleteUsed error: $e")
        }
    }

    override suspend fun resetBudgetUsed(budgetId: Long) {
        try {
            budgetDao.deleteBudgetUsed(budgetId)
        } catch (e: Exception) {
            Logger.e("resetBudgetUsed error: $e")
        }
    }
}
