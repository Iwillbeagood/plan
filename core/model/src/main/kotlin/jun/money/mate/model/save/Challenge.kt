package jun.money.mate.model.save

import java.time.LocalDate

data class Challenge(
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
    val currentAmount get() = progress.sumOf { it.amount }

    val nextProgress: ChallengeProgress?
        get() {
            val lastProgress = progress.find { !it.isAchieved }
            return lastProgress
        }

    val nextDate: LocalDate?
        get() {
            return nextProgress?.date
        }

    fun totalTimes(): String {
        return when (type) {
            is ChallengeType.Monthly -> "/${count}개월"
            is ChallengeType.Weekly -> "/${count}주"
        }
    }

    companion object {
        val sample = Challenge(
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
                    date = LocalDate.now().minusMonths(1),
                ),
                ChallengeProgress(
                    id = 2,
                    challengeId = 1,
                    index = 2,
                    amount = 100_000,
                    isAchieved = true,
                    date = LocalDate.now(),
                ),
                ChallengeProgress(
                    id = 2,
                    challengeId = 1,
                    index = 2,
                    amount = 100_000,
                    isAchieved = true,
                    date = LocalDate.now().plusMonths(1),
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
)

enum class ChallengeProgressType {
    Now,
    PAST,
    UPCOMING
}