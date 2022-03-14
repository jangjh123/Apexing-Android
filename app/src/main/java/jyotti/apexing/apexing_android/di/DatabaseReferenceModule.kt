package jyotti.apexing.apexing_android.di

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseReferenceModule {

    @Provides
    @Singleton
    fun provideDatabaseReference() = Firebase.database.getReference("USER")
}