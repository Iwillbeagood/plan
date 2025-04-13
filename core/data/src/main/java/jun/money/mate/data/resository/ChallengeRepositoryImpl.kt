package jun.money.mate.data.resository

import jun.money.mate.data.mapper.toChallengeEntity
import jun.money.mate.data.mapper.toChallengeProgressEntity
import jun.money.mate.data.mapper.toMoneyChallenge
import jun.money.mate.data_api.database.ChallengeRepository
import jun.money.mate.database.dao.ChallengeDao
import jun.money.mate.database.entity.ChallengeWithProgress
import jun.money.mate.model.save.Challenge
import jun.money.mate.utils.etc.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
    private val challengeDao: ChallengeDao
) : ChallengeRepository {

    override suspend fun upsertChallenge(challenge: Challenge) {
        try {
            challengeDao.upsertChallenge(challenge.toChallengeEntity())
            challengeDao.upsertChallengeProgress(challenge.toChallengeProgressEntity())
        } catch (e: Exception) {
            Logger.e("upsertChallenge error: $e")
        }
    }

    override suspend fun updateChallengeAchieved(challengeId: Long, isAchieved: Boolean) {
        try {
            challengeDao.updateChallengeAchieved(challengeId, isAchieved)
        } catch (e: Exception) {
            Logger.e("updateChallengeAchieved error: $e")
        }
    }

    override fun getChallengeList(): Flow<List<Challenge>> {
        return challengeDao.getAllChallengesWithProgress().map {
            it.map(ChallengeWithProgress::toMoneyChallenge)
                .sortedBy { it.challengeCompleted }
        }.catch {
            Logger.e("getChallengeList error: $it")
        }
    }

    override fun getChallengeById(id: Long): Flow<Challenge> {
        return challengeDao.getChallengeWithProgress(id).map {
            it.toMoneyChallenge()
        }.catch {
            Logger.e("getChallengeById error: $it")
        }
    }

    override suspend fun deleteById(id: Long) {
        try {
            challengeDao.deleteById(id)
        } catch (e: Exception) {
            Logger.e("deleteById error: $e")
        }
    }
}