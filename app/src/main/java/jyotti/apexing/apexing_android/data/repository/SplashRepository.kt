package jyotti.apexing.apexing_android.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import jyotti.apexing.apexing_android.BuildConfig
import jyotti.apexing.data_store.KEY_ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SplashRepository @Inject constructor(
    dataStore: DataStore<Preferences>,
    dispatcher: CoroutineDispatcher,
    val firebaseDatabase: FirebaseDatabase
) {
    private val idFlow: Flow<String> = dataStore.data.map {
        it[KEY_ID] ?: ""
    }.flowOn(dispatcher)

    fun getStoredIdFlow(): Flow<String> = idFlow

    inline fun fetchVersion(
        crossinline onComplete: (Boolean) -> Unit,
        crossinline onFailure: () -> Unit
    ) {
        firebaseDatabase.getReference("VERSION").child("current").get().addOnSuccessListener {
            val newestVersion = it.value as String
            Log.d("remote", newestVersion)
            Log.d("local", BuildConfig.VERSION_NAME)

            if (newestVersion != BuildConfig.VERSION_NAME) {
                onComplete(true) // onTrack
            } else {
                onComplete(false)
            }
        }.addOnCanceledListener {
            onFailure()
        }.addOnFailureListener {
            onFailure()
        }
    }

    inline fun fetchDormancy(   // 휴면 체크
        id: String,
        crossinline onSuccess: (Boolean) -> Unit,
        crossinline onFailure: () -> Unit
    ) {
        firebaseDatabase.getReference("USER").child(id).get().addOnSuccessListener {
            onSuccess(it.child("isDormancy").getValue<Boolean>() ?: false)
        }.addOnCanceledListener {
            onFailure()
        }.addOnFailureListener {
            onFailure()
        }
    }

    fun updateLastConnectionTime(id: String) {
        firebaseDatabase.getReference("USER").child(id).child("lastConnection")
            .setValue("${System.currentTimeMillis() / 1000L}")
    }
}