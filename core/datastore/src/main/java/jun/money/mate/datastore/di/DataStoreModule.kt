package jun.money.mate.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jun.money.mate.datastore.model.UserPreferences
import jun.money.mate.datastore.serializer.UserPreferencesSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val USER_PREFERENCES_DATASTORE_NAME = "USER_PREFERENCES"

    private val Context.userPreferenceDataStore by dataStore(
        USER_PREFERENCES_DATASTORE_NAME, UserPreferencesSerializer
    )

    @Provides
    @Singleton
    fun provideUserPreferenceDataStore(
        @ApplicationContext context: Context
    ): DataStore<UserPreferences> = context.userPreferenceDataStore
}
