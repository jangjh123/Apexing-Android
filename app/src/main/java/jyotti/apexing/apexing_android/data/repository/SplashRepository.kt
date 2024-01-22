package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import jyotti.apexing.apexing_android.data.remote.ApexingApi
import jyotti.apexing.datastore.DATASTORE_KEY_ID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SplashRepository @Inject constructor(
    private val apexingApi: ApexingApi,
    private val datastore: DataStore<Preferences>
) {
    suspend fun fetchVersion(): String = apexingApi.fetchVersion()

    suspend fun readStoredId(): String? {
        return datastore.data.map { preferences ->
            preferences[DATASTORE_KEY_ID]
        }.first()
    }

    suspend fun fetchIsDormancy(id: String): Boolean = apexingApi.fetchIsDormancy(id)

    suspend fun fetchLastConnectedTime(id: String): Boolean = apexingApi.fetchLastConnectedTime(
        id = id,
        lastConnectedTime = "${System.currentTimeMillis() / 1000L}"
    ).isNotEmpty()
}