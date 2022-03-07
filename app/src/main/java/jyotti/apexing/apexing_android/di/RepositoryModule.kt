package jyotti.apexing.apexing_android.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import jyotti.apexing.apexing_android.data.repository.AccountRepository
import jyotti.apexing.apexing_android.data.repository.MainRepository
import jyotti.apexing.apexing_android.data.repository.SplashRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideSplashRepository(datastore: DataStore<Preferences>) =
        SplashRepository(datastore)

    @ViewModelScoped
    @Provides
    fun provideAccountRepository(
        networkManager: NetworkManager,
        datastore: DataStore<Preferences>
    ) = AccountRepository(networkManager, datastore)

    @ViewModelScoped
    @Provides
    fun provideMainRepository(
        networkManager: NetworkManager,
        datastore: DataStore<Preferences>
    ) = MainRepository(networkManager, datastore)
}