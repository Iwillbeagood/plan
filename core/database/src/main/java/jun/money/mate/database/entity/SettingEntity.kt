package jun.money.mate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jun.money.mate.database.AppDatabase
import jun.money.mate.database.util.generateInsertQuery

@Entity(tableName = AppDatabase.SETTINGS_TABLE_NAME)
data class SettingEntity(
    @PrimaryKey val id: Int = 1,
    val isAutoLogin: Boolean,
    val isShowNotification: Boolean,
) {

    companion object {
        val DEFAULT_SETTING = SettingEntity(
            isAutoLogin = true,
            isShowNotification = true
        )

        val INSERT_QUERY = generateInsertQuery(DEFAULT_SETTING, AppDatabase.SETTINGS_TABLE_NAME)
    }
}