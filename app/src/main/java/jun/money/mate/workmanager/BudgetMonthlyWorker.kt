package jun.money.mate.workmanager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import jun.money.mate.dataApi.database.BudgetRepository
import jun.money.mate.model.consumption.Budget
import jun.money.mate.model.consumption.PastBudget
import jun.money.mate.utils.etc.Logger
import kotlinx.coroutines.runBlocking
import java.time.Duration.between
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * 달의 마지막 날, 시간일 경우 사용 기록은 초기화하고 기록은 저장함
 * */
class BudgetMonthlyWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    @Inject
    lateinit var budgetRepository: BudgetRepository

    override fun doWork(): Result {
        runBlocking {
            if (isLastDayOfMonth()) {
                budgetRepository.getBudgetsFlow().collect {
                    it.forEach { budget ->
                        resetBudget(budget)
                        addPastBudget(budget)
                    }
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

// 매일 밤 11시 59분에 작업이 진행
fun scheduleBudgetMonthlyWork(context: Context) {
    val now = LocalDateTime.now()
    val targetTime = LocalDate.now().atTime(23, 59)

    val initialDelay = if (now.toLocalTime().isBefore(targetTime.toLocalTime())) {
        between(now, targetTime)
    } else {
        between(now, targetTime.plusDays(1))
    }

    val workRequest = PeriodicWorkRequestBuilder<BudgetMonthlyWorker>(
        1, TimeUnit.DAYS
    )
        .setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "monthly_budget_worker",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}

private fun isLastDayOfMonth(): Boolean {
    val today = LocalDate.now()
    val lastDayOfMonth = YearMonth.now().atEndOfMonth()
    return today == lastDayOfMonth
}
