package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.database.FirebaseDatabase
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import javax.inject.Inject

class AccountRepository @Inject constructor(
    val networkManager: NetworkManager,
    private val datastore: DataStore<Preferences>,
    private val firebaseDatabase: FirebaseDatabase
) {

    inline fun sendAccountRequest(
        platform: String,
        id: String,
        crossinline onSuccess: (String) -> Unit,
        crossinline onNull: () -> Unit,
        crossinline onError: () -> Unit,
        crossinline onFailure: () -> Unit
    ) {
//        networkManager.getClient().fetchAccount(platform, id, KEY_API).enqueue(object :
//            Callback<JsonObject> {
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                if (response.code() == 404) {
//                    onNull()
//                }
//
//                try {
//                    response.body()!!.get("global").asJsonObject.get("uid").toString().let {
//                        onSuccess(it)
//                    }
//                } catch (exception: Exception) {
//                    try {
//                        val errorMessage = response.body()!!.get("Error").toString()
//
//                        when {
//                            errorMessage.contains("Slow down") -> {
//                                onError()
//                            }
//                            else -> {
//                                onNull()
//                            }
//                        }
//                    } catch (exception: Exception) {
//                        onNull()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                onFailure()
//            }
//        })
    }

    suspend fun storeAccount(platform: String, id: String, uid: String) {
        val user: HashMap<String, String> = HashMap()
        user[id] = platform
        firebaseDatabase.getReference("USER").child(platform)
            .updateChildren(user as Map<String, String>)
        firebaseDatabase.getReference("USER").child("PC").child(id).child("isDormancy")
            .setValue(false)
        firebaseDatabase.getReference("USER").child("PC").child(id).child("lastConnection")
            .setValue(System.currentTimeMillis() / 1000L)
    }
}