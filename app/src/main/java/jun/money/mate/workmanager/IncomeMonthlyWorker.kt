package jun.money.mate.workmanager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import jun.money.mate.dataApi.database.IncomeRepository
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.income.Income
import jun.money.mate.utils.etc.Logger
import kotlinx.coroutines.runBlocking
import java.time.Duration.between
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * 매월 반복되는 수익 경우 오늘이 이번달의 마지막 날일 경우 다음 달의 날짜에 해당 수익 추가
 * */
class IncomeMonthlyWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    @Inject
    lateinit var incomeRepository: IncomeRepository

    override fun doWork(): Result {
        runBlocking {
            if (isLastDayOfMonth()) {
                incomeRepository.getIncomesByMonth(YearMonth.now())
                    .collect {
                        it.incomes.filter { it.dateType == DateType.Monthly }
                            .forEach {
                                addNextMonthIncome(it)
                            }
                    }
            }
        }
        return Result.success()
    }

    private suspend fun addNextMonthIncome(income: Income) {
        Logger.d("addNextMonthSavePlan: $income")
        if (income.addYearMonth == YearMonth.now()) {
            incomeRepository.insertIncome(
                Income(
                    id = System.currentTimeMillis(),
                    addDate = income.addDate.plusMonths(1),
                    dateType = income.dateType,
                    title = income.title,
                    amount = income.amount,
                    date = income.date,
                )
            )
        }
    }
}

// 매일 밤 11시 50분에 작업이 진행
fun scheduleIncomeMonthlyWork(context: Context) {
    val now = LocalDateTime.now()
    val targetTime = LocalDate.now().atTime(23, 50)

    val initialDelay = if (now.toLocalTime().isBefore(targetTime.toLocalTime())) {
        between(now, targetTime)
    } else {
        between(now, targetTime.plusDays(1))
    }

    val workRequest = PeriodicWorkRequestBuilder<SavingMonthlyWorker>(
        1, TimeUnit.DAYS
    )
        .setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "monthly_saving_worker",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}

private fun isLastDayOfMonth(): Boolean {
    val today = LocalDate.now()
    val lastDayOfMonth = YearMonth.now().atEndOfMonth()
    return today == lastDayOfMonth
}
