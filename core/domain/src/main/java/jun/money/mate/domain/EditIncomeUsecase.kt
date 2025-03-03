package jun.money.mate.domain

import jun.money.mate.data_api.database.IncomeRepository
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.Income
import java.time.LocalDate
import javax.inject.Inject

class EditIncomeUsecase @Inject constructor(
    private val incomeRepository: IncomeRepository
) {

    suspend operator fun invoke(
        id: Long,
        title: String,
        amount: Long,
        dateType: DateType?,
        originIncome: Income,
        onSuccess: () -> Unit,
        onError: (MessageType) -> Unit
    ) {
        if (title.isEmpty()) {
            onError(MessageType.Message("수입명을 입력해 주세요"))
            return
        }

        if (amount <= 0) {
            onError(MessageType.Message("수입액을 입력해 주세요"))
            return
        }

        if (dateType == null) {
            onError(MessageType.Message("날짜를 입력해 주세요"))
            return
        }

        incomeRepository.upsertIncome(
            Income(
                id = id,
                title = title,
                amount = amount,
                dateType = when (dateType) {
                    is DateType.Monthly -> dateType.copy()
                    is DateType.Specific -> dateType
                },
            )
        )

        when (val originDateType = originIncome.dateType) {
            is DateType.Monthly -> {
                editOriginMonthlyIncome(originIncome, originDateType)
                addIncome(title, amount, dateType)
            }
            is DateType.Specific -> {
                incomeRepository.upsertIncome(
                    Income(
                        id = id,
                        title = title,
                        amount = amount,
                        dateType = dateType
                    )
                )
            }
        }

        onSuccess()
    }

    private suspend fun editOriginMonthlyIncome(
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

    private suspend fun addIncome(
        title: String,
        amount: Long,
        dateType: DateType
    ) {
        incomeRepository.upsertIncome(
            Income(
                id = System.currentTimeMillis(),
                title = title,
                amount = amount,
                dateType = dateType
            )
        )
    }
}