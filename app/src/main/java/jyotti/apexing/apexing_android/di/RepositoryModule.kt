package jyotti.apexing.apexing_android.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jyotti.apexing.apexing_android.data.local.MatchDao
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import jyotti.apexing.apexing_android.data.repository.AccountRepository
import jyotti.apexing.apexing_android.data.repository.MainRepository
import jyotti.apexing.apexing_android.data.repository.SplashRepository
import jyotti.apexing.apexing_android.data.repository.StatisticsRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideSplashRepository(dataStore: DataStore<Preferences>) =
        SplashRepository(dataStore)

    @ViewModelScoped
    @Provides
    fun provideAccountRepository(
        networkManager: NetworkManager,
        dataStore: DataStore<Preferences>
    ) = AccountRepository(networkManager, dataStore)

    @ViewModelScoped
    @Provides
    fun provideMainRepository(
        networkManager: NetworkManager,
        dataStore: DataStore<Preferences>
    ) = MainRepository(networkManager, dataStore)

    @ViewModelScoped
    @Provides
    fun provideStatisticsRepository(
        networkManager: NetworkManager,
        dataStore: DataStore<Preferences>,
        matchDao: MatchDao
    ) = StatisticsRepository(networkManager, dataStore, matchDao)
}