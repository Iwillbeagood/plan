package jun.money.mate.model.save

import jun.money.mate.model.save.ChallengeType.Monthly
import jun.money.mate.model.save.ChallengeType.Weekly
import java.time.LocalDate

data class MoneyChallenge(
    val id: Long,
    val title: String,
    val count: Int,
    val startDate: LocalDate,
    val goalAmount: Long,
    val type: ChallengeType,
    val progress: List<ChallengeProgress>,
) {
    val challengeCompleted get() = progress.all { it.isAchieved }
    val achievedCount get() = progress.count { it.isAchieved }.toString()

    val nextProgress: ChallengeProgress
        get() {
            val lastProgress = progress.findLast { it.isAchieved } ?: progress.first()
            return lastProgress
        }

    val nextDate: LocalDate
        get() {
            val lastDate = nextProgress.date
            return when (type) {
                is Monthly -> lastDate.plusMonths(1)
                is Weekly -> lastDate.plusWeeks(1)
            }
        }

    fun getInstallmentAmount(): Pair<Long, Long> {
        if (count <= 0) return Pair(0L, 0L)

        val baseInstallment = ((goalAmount / count) / 10_000) * 10_000
        val totalBaseAmount = baseInstallment * count
        val remainingAmount = goalAmount - totalBaseAmount

        return Pair(baseInstallment, remainingAmount)
    }

//    fun getRequiredInstallments(): Int {
//        return if (amount > 0) {
//            ceil(goalAmount.toDouble() / amount).toInt()
//        } else {
//            0
//        }
//    }

    fun totalTimes(): String {
        return when (type) {
            is Monthly -> "/${count}개월"
            is Weekly -> "/${count}주"
        }
    }

    companion object {
        val sample = MoneyChallenge(
            id = 1,
            title = "적금",
            count = 12,
            startDate = LocalDate.now(),
            goalAmount = 1_200_000,
            type = ChallengeType.Monthly(1),
            progress = listOf(
                ChallengeProgress(
                    id = 1,
                    challengeId = 1,
                    index = 1,
                    amount = 100_000,
                    isAchieved = false,
                    date = LocalDate.now(),
                ),
                ChallengeProgress(
                    id = 2,
                    challengeId = 1,
                    index = 2,
                    amount = 100_000,
                    isAchieved = true,
                    date = LocalDate.now(),
                ),
            )
        )
    }
}

data class ChallengeProgress(
    val index: Int,
    val id: Long,
    val challengeId: Long,
    val amount: Long,
    val isAchieved: Boolean,
    val date: LocalDate,
) {

    val dateType: ChallengeDateType
        get() = ChallengeDateType.from(date)
}

enum class ChallengeDateType {
    Today,
    PAST,
    UPCOMING;

    companion object {
        fun from(date: LocalDate): ChallengeDateType {
            val now = LocalDate.now()
            return when {
                date.isBefore(now) -> PAST
                date.isEqual(now) -> Today
                else -> UPCOMING
            }
        }
    }
}