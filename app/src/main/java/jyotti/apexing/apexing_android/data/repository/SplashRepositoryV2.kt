package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import jyotti.apexing.apexing_android.data.remote.ApexingApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SplashRepositoryV2 @Inject constructor(
    private val apexingApi: ApexingApi,
    private val dataStore: DataStore<Preferences>
) {
    fun fetchVersion(): Flow<String> = flow {
        emit(apexingApi.fetchVersion())
    }
}