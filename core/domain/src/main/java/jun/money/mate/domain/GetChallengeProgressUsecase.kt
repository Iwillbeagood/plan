package jun.money.mate.domain

import jun.money.mate.dataApi.database.ChallengeRepository
import jun.money.mate.model.save.SavingChallenge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.YearMonth
import javax.inject.Inject

class GetChallengeProgressUsecase @Inject constructor(
    private val challengeRepository: ChallengeRepository,
) {

    operator fun invoke(yearMonth: YearMonth): Flow<List<SavingChallenge>> = flow {
        val mutableList = mutableListOf<SavingChallenge>()
        challengeRepository.getChallengeList().collect { challengeList ->
            challengeList.forEach { challenge ->
                challenge.progress.filter {
                    it.date.year == yearMonth.year && it.date.month == yearMonth.month
                }.forEach {
                    mutableList.add(
                        SavingChallenge(
                            title = challenge.title + " ${it.index}회차",
                            day = it.date.dayOfMonth,
                            amount = it.amount,
                        ),
                    )
                }
            }
            emit(mutableList)
        }
    }
}
