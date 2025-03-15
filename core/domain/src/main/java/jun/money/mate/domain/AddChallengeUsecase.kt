package jun.money.mate.domain

import jun.money.mate.data_api.database.ChallengeRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.model.save.ChallengeProgress
import jun.money.mate.model.save.ChallengeType
import jun.money.mate.model.save.ChallengeType.Companion.nthPaymentDate
import jun.money.mate.model.save.MoneyChallenge
import java.time.LocalDate
import javax.inject.Inject

class AddChallengeUsecase @Inject constructor(
    private val challengeRepository: ChallengeRepository
) {

    suspend operator fun invoke(
        title: String,
        goalAmount: Long,
        amount: Long,
        count: Int,
        challengeType: ChallengeType?,
        onSuccess: () -> Unit,
        onError: (MessageType) -> Unit
    ) {

        if (goalAmount <= 0) {
            onError(MessageType.Message("도전 금액을 입력해 주세요"))
            return
        }

        if (amount <= 0) {
            onError(MessageType.Message("납입 금액을 입력해 주세요"))
            return
        }

        if (challengeType == null) {
            onError(MessageType.Message("도전 주기를 선택해 주세요"))
            return
        }

        val parentId = System.currentTimeMillis()

        challengeRepository.upsertChallenge(
            MoneyChallenge(
                id = parentId,
                title = title,
                count = count,
                startDate = LocalDate.now(),
                goalAmount = goalAmount,
                type = challengeType,
                progress = getProgress(parentId, count, amount, goalAmount, challengeType)
            )
        )
        onSuccess()
    }

    private fun getProgress(
        challengeId: Long,
        count: Int,
        amount: Long,
        goalAmount: Long,
        challengeType: ChallengeType,
    ): List<ChallengeProgress> {
        val remainAmount = goalAmount - amount * count
        val progress = mutableListOf<ChallengeProgress>()


        for (i in 1..count) {
            if (i == count && remainAmount != 0L) {
                progress.add(
                    ChallengeProgress(
                        challengeId = challengeId,
                        index = i,
                        amount = remainAmount,
                        date = challengeType.nthPaymentDate(i),
                    )
                )
            } else {
                progress.add(
                    ChallengeProgress(
                        challengeId = challengeId,
                        index = i,
                        amount = amount,
                        date = challengeType.nthPaymentDate(i),
                    )
                )
            }
        }
        return progress
    }
}