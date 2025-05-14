package jun.money.mate.domain

import jun.money.mate.dataApi.database.IncomeRepository
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.income.Income
import java.time.YearMonth
import javax.inject.Inject

class DeleteIncomeUsecase @Inject constructor(
    private val incomeRepository: IncomeRepository,
) {

    suspend operator fun invoke(
        incomes: List<Income>,
        onSuccess: () -> Unit,
    ) {
        incomes.forEach { income ->
            incomeRepository.deleteById(income.id)
        }

        onSuccess()
    }
}
