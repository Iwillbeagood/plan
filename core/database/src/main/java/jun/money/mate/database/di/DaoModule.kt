package jun.money.mate.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jun.money.mate.database.AppDatabase
import jun.money.mate.database.dao.NoticePopUpDao
import jun.money.mate.database.dao.PushDao
import jun.money.mate.database.dao.SettingDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    @Singleton
    fun providesNoticePopUpDao(
        database: AppDatabase,
    ): NoticePopUpDao = database.noticePopUpDao()

    @Provides
    @Singleton
    fun provideSettingDao(
        database: AppDatabase,
    ) : SettingDao = database.settingDao()

    @Provides
    @Singleton
    fun providePushDao(
        database: AppDatabase,
    ) : PushDao = database.pushDao()
}
