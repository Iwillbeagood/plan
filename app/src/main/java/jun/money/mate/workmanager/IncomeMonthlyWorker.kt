package jun.money.mate.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import jun.money.mate.dataApi.database.IncomeRepository
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.income.Income
import jun.money.mate.utils.etc.Logger
import jun.money.mate.workmanager.util.isLastDayOfMonth
import java.time.YearMonth

/**
 * 매월 반복되는 수익 경우 오늘이 이번달의 마지막 날일 경우 다음 달의 날짜에 해당 수익 추가
 * */
@HiltWorker
class IncomeMonthlyWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val incomeRepository: IncomeRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Logger.d("IncomeMonthlyWorker")
        if (isLastDayOfMonth()) {
            incomeRepository.getIncomesByMonth(YearMonth.now())
                .collect {
                    it.incomes.forEach { income ->
                        if (income.dateType == DateType.Monthly) {
                            addNextMonthIncome(income)
                        }
                    }
                }
        }
        return Result.success()
    }

    private suspend fun addNextMonthIncome(income: Income) {
        if (income.addYearMonth == YearMonth.now()) {
            Logger.d("addNextMonthSavePlan: $income")
            incomeRepository.insertIncome(
                Income(
                    id = System.currentTimeMillis(),
                    addDate = income.addDate.plusMonths(1),
                    dateType = income.dateType,
                    title = income.title,
                    amount = income.amount,
                    date = income.date,
                ),
            )
        }
    }
}
