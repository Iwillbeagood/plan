package jun.money.mate.data.mapper

import jun.money.mate.database.entity.ChallengeEntity
import jun.money.mate.database.entity.ChallengeProgressEntity
import jun.money.mate.database.entity.ChallengeWithProgress
import jun.money.mate.model.save.ChallengeProgress
import jun.money.mate.model.save.MoneyChallenge

fun MoneyChallenge.toChallengeEntity() = ChallengeEntity(
    id = id,
    title = title,
    startDate = startDate,
    count = count,
    goalAmount = goalAmount,
    type = type
)

fun MoneyChallenge.toChallengeProgressEntity() = progress.map {
    ChallengeProgressEntity(
        index = it.index,
        challengeId = id,
        amount = it.amount,
        isAchieved = it.isAchieved,
        date = it.date
    )
}

fun ChallengeWithProgress.toMoneyChallenge() = MoneyChallenge(
    id = challenge.id,
    title = challenge.title,
    startDate = challenge.startDate,
    count = challenge.count,
    goalAmount = challenge.goalAmount,
    type = challenge.type,
    progress = progressList.map {
        ChallengeProgress(
            id = it.id,
            challengeId = it.challengeId,
            index = it.index,
            amount = it.amount,
            isAchieved = it.isAchieved,
            date = it.date,
        )
    }
)