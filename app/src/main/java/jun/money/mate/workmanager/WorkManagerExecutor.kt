package jun.money.mate.workmanager

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import jun.money.mate.utils.etc.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration.between
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerExecutor @Inject constructor(
    private val workManager: WorkManager,
) {

    fun execute() {
        scheduleMonthlyWork<BudgetMonthlyWorker>()
        scheduleMonthlyWork<IncomeMonthlyWorker>()
        scheduleMonthlyWork<SavingMonthlyWorker>()
    }

    fun startOneTimeWork() {
        val workRequest = OneTimeWorkRequestBuilder<BudgetMonthlyWorker>().build()
        workManager.enqueue(workRequest)
    }

    private inline fun <reified W : ListenableWorker> scheduleMonthlyWork() {
        val now = LocalDateTime.now()
        val targetTime = LocalDate.now().atTime(23, 0)

        val initialDelay = if (now.toLocalTime().isBefore(targetTime.toLocalTime())) {
            between(now, targetTime)
        } else {
            between(now, targetTime.plusDays(1))
        }

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .setRequiresStorageNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<W>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
            .build()

        val name = W::class.java.simpleName

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = name,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
            request = workRequest
        )

        observeWorkManager(name)
    }


    private fun observeWorkManager(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            workManager.getWorkInfosForUniqueWorkFlow(name).collect { workInfoList ->
                if (workInfoList.isNotEmpty()) {
                    val workInfo = workInfoList.first()

                    workInfo.let {
                        when (it.state) {
                            WorkInfo.State.ENQUEUED -> { Logger.i("작업 대기 중") }
                            WorkInfo.State.RUNNING -> { Logger.i("작업 실행 중") }
                            WorkInfo.State.SUCCEEDED -> { Logger.i("작업 성공") }
                            WorkInfo.State.FAILED -> { Logger.e("작업 실패") }
                            WorkInfo.State.CANCELLED -> { Logger.e("작업 취소됨") }
                            WorkInfo.State.BLOCKED -> { Logger.i("작업이 블록 상태임") }
                        }
                    }
                }
            }
        }
    }
}