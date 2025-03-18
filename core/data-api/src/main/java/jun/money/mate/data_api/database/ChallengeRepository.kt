package jun.money.mate.data_api.database

import jun.money.mate.model.save.Challenge
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {
    suspend fun upsertChallenge(challenge: Challenge)
    suspend fun updateChallengeAchieved(challengeId: Long, isAchieved: Boolean)
    fun getChallengeList(): Flow<List<Challenge>>
    fun getChallengeById(id: Long): Flow<Challenge>
    suspend fun deleteById(id: Long)
}