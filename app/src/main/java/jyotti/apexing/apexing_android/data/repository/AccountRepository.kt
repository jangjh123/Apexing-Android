package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import jyotti.apexing.apexing_android.BuildConfig
import jyotti.apexing.apexing_android.data.remote.ApexLegendsApiError
import jyotti.apexing.apexing_android.data.remote.ApexingApi
import jyotti.apexing.datastore.DATASTORE_KEY_ID
import jyotti.apexing.datastore.DATASTORE_KEY_UID
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val apexingApi: ApexingApi,
    private val dataStore: DataStore<Preferences>
) {
    suspend fun fetchOriginAccount(id: String): String {
        val response = apexingApi.fetchAccount("https://api.mozambiquehe.re/bridge?version=5&platform=PC&player=$id&auth=${BuildConfig.KEY_API}")

        if (response.isSuccessful) {
            return response.body()?.asJsonObject?.get(RESPONSE_KEY_GLOBAL)?.asJsonObject?.get(RESPONSE_KEY_UID).toString().replace("\"", "")
        } else {
            response.errorBody()?.string()?.let { errorBody ->
                if (errorBody.contains(ERROR_SLOW_DOWN)) {
                    throw ApexLegendsApiError.SlowDown
                } else if (errorBody.contains(PLAYER_NOT_FOUND)) {
                    throw ApexLegendsApiError.PlayerNotFound
                } else {
                    throw ApexLegendsApiError.Unknown
                }
            } ?: throw ApexLegendsApiError.Unknown
        }
    }

    suspend fun storeAccount(
        id: String,
        uid: String
    ) {
        dataStore.edit {
            it[DATASTORE_KEY_ID] = id
            it[DATASTORE_KEY_UID] = uid
        }
    }

    companion object {
        private const val RESPONSE_KEY_GLOBAL = "global"
        private const val RESPONSE_KEY_UID = "uid"
        private const val ERROR_SLOW_DOWN = "Slow down"
        private const val PLAYER_NOT_FOUND = "Player not found"
    }
}