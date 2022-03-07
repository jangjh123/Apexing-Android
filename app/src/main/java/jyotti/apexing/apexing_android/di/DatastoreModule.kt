package jyotti.apexing.apexing_android.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jyotti.apexing.datastore.dataStore
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatastoreModule {

    @Singleton
    @Provides
    fun provideAccountDatastore(@ApplicationContext context: Context) = context.dataStore
}