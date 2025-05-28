package jun.money.mate.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import jun.money.mate.dataApi.database.SaveRepository
import jun.money.mate.model.save.SavePlan
import jun.money.mate.model.save.SavingsType
import jun.money.mate.utils.etc.Logger
import jun.money.mate.workmanager.util.isLastDayOfMonth
import java.time.YearMonth

/**
 * 매월 반복되는 계획의 경우 오늘이 이번달의 마지막 날일 경우 다음 달의 날짜에 해당 저축 추가
 * */
@HiltWorker
class SavingMonthlyWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val saveRepository: SaveRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Logger.d("SavingMonthlyWorker")
        if (isLastDayOfMonth()) {
            val list = saveRepository.getSavePlanListByMonth(YearMonth.now())
            list.savePlans
                .filter { it.savingsType is SavingsType.PaidCount }
                .forEach {
                    addNextMonthSavePlan(it)
                }
        }
        return Result.success()
    }

    private suspend fun addNextMonthSavePlan(savePlan: SavePlan) {
        if (savePlan.addYearMonth == YearMonth.now()) {
            Logger.d("addNextMonthSavePlan: $savePlan")
            saveRepository.upsertSavePlan(
                savePlan.copy(
                    id = System.currentTimeMillis(),
                    addYearMonth = YearMonth.now().plusMonths(1),
                    executed = true,
                ),
            )
        }
    }
}
