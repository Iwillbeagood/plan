package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase
import jun.money.mate.database.entity.NoticePopUpEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface NoticePopUpDao {

    @Upsert
    suspend fun upsertNoticePopUp(noticePopUpEntity: NoticePopUpEntity)

    @Query("SELECT * FROM ${AppDatabase.NOTICE_POP_UP_TABLE_NAME} WHERE id = 1")
    fun getNoticePopUp(): Flow<NoticePopUpEntity>

    @Query("DELETE FROM ${AppDatabase.NOTICE_POP_UP_TABLE_NAME} WHERE id = 1")
    suspend fun deleteNoticePopUp()

    @Query("UPDATE ${AppDatabase.NOTICE_POP_UP_TABLE_NAME} SET startDate = :startDateTime WHERE id = 1")
    suspend fun updateNoticePopUpStartDate(startDateTime: LocalDateTime)
}