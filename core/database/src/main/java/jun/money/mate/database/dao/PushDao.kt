package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase
import jun.money.mate.database.entity.PushEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PushDao {

    @Upsert
    suspend fun upsertPush(pushEntity: PushEntity)

    @Query("SELECT * from ${AppDatabase.PUSH_TABLE_NAME}")
    fun getPushes(): Flow<List<PushEntity>>

    @Query("UPDATE ${AppDatabase.PUSH_TABLE_NAME} SET isRead = :isRead WHERE id = :id")
    suspend fun updatePushWatchedState(id: String, isRead: Boolean)

    @Query("UPDATE ${AppDatabase.PUSH_TABLE_NAME} SET isRead = :isRead")
    suspend fun updateAllPushWatchedState(isRead: Boolean)

    @Query("DELETE FROM ${AppDatabase.PUSH_TABLE_NAME} WHERE id = :id")
    suspend fun deletePush(id: String)
}