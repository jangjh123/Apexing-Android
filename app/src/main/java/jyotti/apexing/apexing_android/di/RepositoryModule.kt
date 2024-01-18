package jyotti.apexing.apexing_android.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jyotti.apexing.apexing_android.data.local.MatchDao
import jyotti.apexing.apexing_android.data.remote.ApexingApi
import jyotti.apexing.apexing_android.data.remote.NetworkManager
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