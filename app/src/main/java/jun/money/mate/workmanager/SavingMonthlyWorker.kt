package jun.money.mate.workmanager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import jun.money.mate.dataApi.database.SaveRepository
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavingsType
import jun.money.mate.utils.etc.Logger
import kotlinx.coroutines.runBlocking
import java.time.Duration.between
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * 매월 반복되는 계획의 경우 오늘이 이번달의 마지막 날일 경우 다음 달의 날짜에 해당 저축 추가
 * */
class SavingMonthlyWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    @Inject
    lateinit var saveRepository: SaveRepository

    override fun doWork(): Result {
        runBlocking {
            if (isLastDayOfMonth()) {
                val list = saveRepository.getSavePlanListByMonth(YearMonth.now())
                list.savePlans
                    .filter { it.savingsType is SavingsType.PaidCount }
                    .forEach {
                        addNextMonthSavePlan(it)
                    }
            }
        }
        return Result.success()
    }

    private suspend fun addNextMonthSavePlan(savePlan: SavePlan) {
        Logger.d("addNextMonthSavePlan: $savePlan")
        if (savePlan.addYearMonth == YearMonth.now()) {
            saveRepository.upsertSavePlan(
                savePlan.copy(
                    id = System.currentTimeMillis(),
                    addYearMonth = YearMonth.now().plusMonths(1),
                    executed = true
                )
            )
        }
    }
}

// 매일 밤 11시 59분에 작업이 진행
fun scheduleSavingMonthlyWork(context: Context) {
    val now = LocalDateTime.now()
    val targetTime = LocalDate.now().atTime(23, 59)

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
