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
import java.time.Duration.between
import java.time.LocalDate
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
        CoroutineScope(Dispatchers.IO).launch {
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
    val today = LocalDate.now()
    val lastDayOfMonth = YearMonth.now().atEndOfMonth()

    val delay = between(today.atStartOfDay(), lastDayOfMonth.atTime(23, 59)).toMillis()

    val workRequest = OneTimeWorkRequestBuilder<SavingMonthlyWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}

private fun isLastDayOfMonth(): Boolean {
    val today = LocalDate.now()
    val lastDayOfMonth = YearMonth.now().atEndOfMonth()
    return today == lastDayOfMonth
}