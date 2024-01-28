package jyotti.apexing.apexing_android.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jyotti.apexing.apexing_android.data.remote.ApexingApi
import jyotti.apexing.apexing_android.data.repository.*
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideSplashRepository(
        apexingApi: ApexingApi,
        dataStore: DataStore<Preferences>
    ): SplashRepository = SplashRepository(
        apexingApi = apexingApi,
        datastore = dataStore
    )

    @ViewModelScoped
    @Provides
    fun provideAccountRepository(
        apexingApi: ApexingApi,
        dataStore: DataStore<Preferences>
    ) = AccountRepository(
        apexingApi = apexingApi,
        dataStore = dataStore
    )

    @ViewModelScoped
    @Provides
    fun provideMainRepository(
        apexingApi: ApexingApi
    ) = MainRepository(apexingApi)

    @ViewModelScoped
    @Provides
    fun provideStatisticsRepository(
        apexingApi: ApexingApi,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ) = StatisticsRepository(
        apexingApi = apexingApi,
        defaultDispatcher = defaultDispatcher
    )
}