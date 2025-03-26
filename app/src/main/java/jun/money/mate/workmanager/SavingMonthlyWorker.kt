package jun.money.mate.workmanager

import android.content.Context
import androidx.work.*
import jun.money.mate.data_api.database.SaveRepository
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavingsType
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.Duration.between
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.concurrent.TimeUnit
import javax.inject.Inject

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
        saveRepository.upsertSavePlan(
            savePlan.copy(
                id = System.currentTimeMillis(),
                addYearMonth = YearMonth.now().plusMonths(1),
                executed = true
            )
        )
    }
}

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
