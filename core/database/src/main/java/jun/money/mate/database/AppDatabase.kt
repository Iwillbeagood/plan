package jun.money.mate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jun.money.mate.database.converter.LocalDateConverter
import jun.money.mate.database.converter.LocalDateTimeConverter
import jun.money.mate.database.dao.NoticePopUpDao
import jun.money.mate.database.dao.PushDao
import jun.money.mate.database.dao.SettingDao
import jun.money.mate.database.entity.NoticePopUpEntity
import jun.money.mate.database.entity.PushEntity
import jun.money.mate.database.entity.SettingEntity

@Database(
    entities = [
        NoticePopUpEntity::class,
        SettingEntity::class,
        PushEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        LocalDateTimeConverter::class,
        LocalDateConverter::class
    ]
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun noticePopUpDao(): NoticePopUpDao
    abstract fun settingDao(): SettingDao
    abstract fun pushDao(): PushDao

    companion object {
        const val DATABASE_NAME = "app_database"

        const val SETTINGS_TABLE_NAME = "settings"
        const val NOTICE_POP_UP_TABLE_NAME = "notice_pop_up"
        const val PUSH_TABLE_NAME = "pushes"
    }
}