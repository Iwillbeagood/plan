package jun.money.mate.data_api.database

import jun.money.mate.model.save.MoneyChallenge
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {
    suspend fun upsertChallenge(challenge: MoneyChallenge)
    suspend fun updateChallengeAchieved(challengeId: Long, isAchieved: Boolean)
    fun getChallengeList(): Flow<List<MoneyChallenge>>
    suspend fun getChallengeById(id: Long): MoneyChallenge
    suspend fun deleteById(id: Long)
}