package jyotti.apexing.apexing_android.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jyotti.apexing.apexing_android.data.local.MatchDao
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import jyotti.apexing.apexing_android.data.repository.*
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
        firebaseDatabase: FirebaseDatabase
    ) = AccountRepository(networkManager, dataStore, firebaseDatabase)

    @ViewModelScoped
    @Provides
    fun provideMainRepository(
        networkManager: NetworkManager,
        firebaseDatabase: FirebaseDatabase,
        dataStore: DataStore<Preferences>,
        matchDao: MatchDao,
        dispatcher: CoroutineDispatcher
    ) = MainRepository(networkManager, firebaseDatabase, dataStore, matchDao, dispatcher)

    @ViewModelScoped
    @Provides
    fun provideStatisticsRepository(
        networkManager: NetworkManager,
        dataStore: DataStore<Preferences>,
        matchDao: MatchDao,
        dispatcher: CoroutineDispatcher
    ) = StatisticsRepository(networkManager, dataStore, matchDao, dispatcher)

    @ViewModelScoped
    @Provides
    fun provideStoreRepository(
        firebaseDatabase: FirebaseDatabase
    ) = StoreRepository(firebaseDatabase)
}