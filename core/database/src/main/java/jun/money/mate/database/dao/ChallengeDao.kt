package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase.Companion.CHALLENGE_PROGRESS_TABLE_NAME
import jun.money.mate.database.AppDatabase.Companion.CHALLENGE_TABLE_NAME
import jun.money.mate.database.entity.ChallengeEntity
import jun.money.mate.database.entity.ChallengeProgressEntity
import jun.money.mate.database.entity.ChallengeWithProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {

    @Upsert
    suspend fun upsertChallenge(entity: ChallengeEntity)

    @Upsert
    suspend fun upsertChallengeProgress(entity: List<ChallengeProgressEntity>)

    @Query("UPDATE $CHALLENGE_PROGRESS_TABLE_NAME SET isAchieved = :isAchieved WHERE id = :challengeId")
    suspend fun updateChallengeAchieved(challengeId: Long, isAchieved: Boolean)

    @Transaction
    @Query("SELECT * FROM $CHALLENGE_TABLE_NAME")
    fun getAllChallengesWithProgress(): Flow<List<ChallengeWithProgress>>

    @Transaction
    @Query("SELECT * FROM $CHALLENGE_TABLE_NAME WHERE id = :challengeId")
    fun getChallengeWithProgress(challengeId: Long): Flow<ChallengeWithProgress>

    @Query("DELETE FROM $CHALLENGE_TABLE_NAME WHERE id = :id")
    suspend fun deleteById(id: Long)
}