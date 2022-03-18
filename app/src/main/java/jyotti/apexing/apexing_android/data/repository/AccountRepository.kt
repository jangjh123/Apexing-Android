package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.database.DatabaseReference
import com.google.gson.JsonObject
import jyotti.apexing.apexing_android.BuildConfig.KEY_API
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import jyotti.apexing.data_store.KEY_ID
import jyotti.apexing.data_store.KEY_PLATFORM
import jyotti.apexing.data_store.KEY_UID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AccountRepository @Inject constructor(
    val networkManager: NetworkManager,
    private val dataStore: DataStore<Preferences>,
    private val databaseRef: DatabaseReference
) {

    inline fun sendAccountRequest(
        platform: String,
        id: String,
        crossinline onSuccess: (String) -> Unit,
        crossinline onNull: () -> Unit,
        crossinline onError: () -> Unit,
        crossinline onFailure: () -> Unit
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
                    405 -> {
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
            it[KEY_PLATFORM] = platform
        }
        dataStore.edit {
            it[KEY_ID] = id
        }
        dataStore.edit {
            it[KEY_UID] = uid
        }

        val user: HashMap<String, String> = HashMap()
        user[id] = platform
        databaseRef.child(platform).updateChildren(user as Map<String, String>)
    }
}