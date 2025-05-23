package jun.money.mate.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import jun.money.mate.dataApi.database.BudgetRepository
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.consumption.PastBudget
import jun.money.mate.utils.etc.Logger
import jun.money.mate.workmanager.util.isLastDayOfMonth
import java.time.YearMonth

/**
 * 달의 마지막 날, 시간일 경우 사용 기록은 초기화하고 기록은 저장함
 * */
@HiltWorker
class BudgetMonthlyWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val budgetRepository: BudgetRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Logger.d("BudgetMonthlyWorker")
        if (isLastDayOfMonth()) {
            budgetRepository.getBudgetsFlow().collect {
                it.forEach { budget ->
                    resetBudget(budget)
                    addPastBudget(budget)
                }
            }
        }
        return Result.success()
    }

    private suspend fun resetBudget(budget: Budget) {
        Logger.d("resetBudget: $budget")
        budgetRepository.resetBudgetUsed(budget.id)
    }

    private suspend fun addPastBudget(budget: Budget) {
        val pastBudget = PastBudget(
            budget = budget.budget,
            amountUsed = budget.amountUsed,
            date = YearMonth.now()
        )
        Logger.d("addPastBudget: $pastBudget")
        budgetRepository.insertPastBudget(pastBudget, budget.id)
    }
}
