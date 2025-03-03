package jun.money.mate.domain

import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.income.Income
import java.time.LocalDate
import javax.inject.Inject

class DeleteIncomeUsecase @Inject constructor(
    private val incomeRepository: IncomeRepository
) {

    suspend operator fun invoke(
        incomes: List<Income>,
        onSuccess: () -> Unit,
    ) {
        incomes.forEach { income ->
            when (val dateType = income.dateType) {
                is DateType.Monthly -> deleteOriginMonthlyIncome(income, dateType)
                is DateType.Specific -> incomeRepository.deleteById(income.id)
            }
        }

        onSuccess()
    }

    private suspend fun deleteOriginMonthlyIncome(
        originIncome: Income,
        dateType: DateType.Monthly
    ) {
        if (dateType.addDate.year == LocalDate.now().year && dateType.addDate.month == LocalDate.now().month) {
            incomeRepository.deleteById(originIncome.id)
            return
        }

        incomeRepository.upsertIncome(
            originIncome.copy(
                dateType = dateType.copy(
                    expiredDate = LocalDate.now().withDayOfMonth(1)
                )
            )
        )
    }
}