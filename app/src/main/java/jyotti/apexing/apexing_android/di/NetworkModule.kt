package jyotti.apexing.apexing_android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jyotti.apexing.apexing_android.BuildConfig.BASE_URL
import jyotti.apexing.apexing_android.data.remote.ApiService
import jyotti.apexing.apexing_android.data.remote.FirebaseRequestInterceptor
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val CONNECT_TIMEOUT_SEC = 5L
    private const val READ_TIMEOUT_SEC = 10L
    private const val WRITE_TIMEOUT_SEC = 5L

    @Provides
    @Singleton
    fun provideNetworkManager() = NetworkManager()

    @Singleton
    @Provides
    fun provideFirebaseRequestInterceptor(): FirebaseRequestInterceptor = FirebaseRequestInterceptor()

    @Singleton
    @Provides
    fun provideOkHttpClient(firebaseRequestInterceptor: FirebaseRequestInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor(firebaseRequestInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}