package jun.money.mate.domain

import jun.money.mate.dataApi.database.IncomeRepository
import jun.money.mate.model.etc.DateType
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.income.Income
import java.time.LocalDate
import javax.inject.Inject

class AddIncomeUsecase @Inject constructor(
    private val incomeRepository: IncomeRepository,
) {

    suspend operator fun invoke(
        title: String,
        amount: Long,
        day: Int,
        dateType: DateType?,
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

        if (dateType == null) {
            onError(MessageType.Message("날짜를 입력해 주세요"))
            return
        }

        incomeRepository.insertIncome(
            Income(
                id = System.currentTimeMillis(),
                title = title,
                amount = amount,
                date = day,
                dateType = dateType,
                addDate = LocalDate.now(),
            ),
        )
        onSuccess()
    }
}
