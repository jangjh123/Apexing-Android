package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import jyotti.apexing.apexing_android.data.remote.ApexingApi
import jyotti.apexing.datastore.DATASTORE_KEY_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SplashRepositoryV2 @Inject constructor(
    private val apexingApi: ApexingApi,
    private val datastore: DataStore<Preferences>
) {
    fun fetchVersion(): Flow<String> = flow {
        emit(apexingApi.fetchVersion())
    }

    fun readStoredId(): Flow<String?> = datastore.data.map { preferences ->
        preferences[DATASTORE_KEY_ID]
    }

    fun fetchIsDormancy(id: String): Flow<Boolean?> = flow {
        emit(apexingApi.fetchIsDormancy(id))
    }

    fun fetchLastConnectedTime(id: String): Flow<String?> = flow {
        emit(apexingApi.fetchLastConnectedTime(id, "${System.currentTimeMillis() / 1000L}"))
    }
}