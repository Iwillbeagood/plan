package jun.money.mate.domain

import jun.money.mate.dataApi.database.IncomeRepository
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.Income
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class EditIncomeUsecase @Inject constructor(
    private val incomeRepository: IncomeRepository,
) {

    suspend operator fun invoke(
        id: Long,
        title: String,
        amount: Long,
        date: Int,
        dateType: DateType,
        addDate: LocalDate,
        onSuccess: () -> Unit,
        onError: (MessageType) -> Unit,
    ) {
        if (title.isEmpty()) {
            onError(MessageType.Message("수입명을 입력해 주세요"))
            return
        }

        if (amount <= 0) {
            onError(MessageType.Message("수입액을 입력해 주세요"))
            return
        }

        incomeRepository.updateIncome(
            Income(
                id = id,
                title = title,
                amount = amount,
                date = date,
                dateType = dateType,
                addDate = addDate
            )
        )

        onSuccess()
    }
}
