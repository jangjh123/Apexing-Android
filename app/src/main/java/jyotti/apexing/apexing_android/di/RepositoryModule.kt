package jyotti.apexing.apexing_android.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.database.DatabaseReference
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
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideSplashRepository(
        dataStore: DataStore<Preferences>,
        dispatcher: CoroutineDispatcher
    ) =
        SplashRepository(dataStore, dispatcher)

    @ViewModelScoped
    @Provides
    fun provideAccountRepository(
        networkManager: NetworkManager,
        dataStore: DataStore<Preferences>,
        databaseRef: DatabaseReference
    ) = AccountRepository(networkManager, dataStore, databaseRef)

    @ViewModelScoped
    @Provides
    fun provideMainRepository(
        networkManager: NetworkManager,
        databaseRef: DatabaseReference,
        dataStore: DataStore<Preferences>,
        matchDao: MatchDao,
        dispatcher: CoroutineDispatcher
    ) = MainRepository(networkManager, databaseRef, dataStore, matchDao, dispatcher)

    @ViewModelScoped
    @Provides
    fun provideStatisticsRepository(
        networkManager: NetworkManager,
        dataStore: DataStore<Preferences>,
        matchDao: MatchDao,
        dispatcher: CoroutineDispatcher
    ) = StatisticsRepository(networkManager, dataStore, matchDao, dispatcher)
}