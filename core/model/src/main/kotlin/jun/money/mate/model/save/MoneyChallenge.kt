package jun.money.mate.model.save

import java.time.LocalDate

data class MoneyChallenge(
    val id: Long = 0,
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
                is ChallengeType.Monthly -> lastDate.plusMonths(1)
                is ChallengeType.Weekly -> lastDate.plusWeeks(1)
            }
        }

    fun totalTimes(): String {
        return when (type) {
            is ChallengeType.Monthly -> "/${count}개월"
            is ChallengeType.Weekly -> "/${count}주"
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
    val id: Long = 0,
    val index: Int,
    val challengeId: Long,
    val amount: Long,
    val date: LocalDate,
    val isAchieved: Boolean = false,
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