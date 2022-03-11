package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import jyotti.apexing.apexing_android.BuildConfig.KEY_API
import com.google.gson.JsonObject
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import jyotti.apexing.data_store.KEY_ID
import jyotti.apexing.data_store.KEY_PLATFORM
import jyotti.apexing.data_store.KEY_UID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.NullPointerException
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val networkManager: NetworkManager,
    private val dataStore: DataStore<Preferences>
) {

    fun sendAccountRequest(
        platform: String,
        id: String,
        onSuccess: (String) -> Unit,
        onNull: () -> Unit,
        onError: () -> Unit,
        onFailure: () -> Unit
    ) {
        networkManager.getClient().fetchAccount(platform, id, KEY_API).enqueue(object :
            Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                when (response.code()) {
                    200 -> {
                        try {
                            onSuccess(
                                response.body()!!.get("global").asJsonObject.get("uid").asString
                            )
                        } catch (exception: Exception) {
                            if (exception is NullPointerException) {
                                onNull()
                            }
                        }
                    }
                    102 -> {
                        onNull()
                    }
                    else -> {
                        onError()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure()
            }

        })
    }

    suspend fun storeAccount(platform: String, id: String, uid: String) {
        dataStore.edit {
            it
            it[KEY_PLATFORM] = platform
        }
        dataStore.edit {
            it[KEY_ID] = id
        }
        dataStore.edit {
            it[KEY_UID] = uid
        }
    }
}