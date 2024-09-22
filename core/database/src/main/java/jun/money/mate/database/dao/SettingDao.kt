package jun.money.mate.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import jun.money.mate.database.AppDatabase
import jun.money.mate.database.entity.SettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {

    @Upsert
    suspend fun upsertSetting(settingEntity: SettingEntity)

    @Query("SELECT * from ${AppDatabase.SETTINGS_TABLE_NAME} WHERE id = 1")
    fun getSettingFlow(): Flow<SettingEntity>

    @Query("UPDATE ${AppDatabase.SETTINGS_TABLE_NAME} SET isAutoLogin = :newValue WHERE id = 1")
    suspend fun updateAutoLogin(newValue: Boolean)

    @Query("UPDATE ${AppDatabase.SETTINGS_TABLE_NAME} SET isShowNotification = :newValue WHERE id = 1")
    suspend fun updateShowNotification(newValue: Boolean)
}