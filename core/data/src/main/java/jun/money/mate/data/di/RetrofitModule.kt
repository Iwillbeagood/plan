package jun.money.mate.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RetrofitModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(HttpNetworkLogger())
            .build()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideConverterFactory(json: Json): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

//    @Provides
//    @Singleton
//    fun provideKTCRetrofit(
//        okHttpClient: OkHttpClient,
//        converterFactory: Converter.Factory
//    ): Retrofit =
//        Retrofit.Builder()
//            .baseUrl(Constants.BASE_URI)
//            .addCallAdapterFactory(ResultCallAdapterFactory.create())
//            .addConverterFactory(converterFactory)
//            .client(okHttpClient).build()
}

