package jyotti.apexing.apexing_android.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jyotti.apexing.apexing_android.BuildConfig
import jyotti.apexing.data_store.KEY_PLATFORM
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SplashRepository @Inject constructor(
    dataStore: DataStore<Preferences>,
    dispatcher: CoroutineDispatcher
) {
    val reference = FirebaseDatabase.getInstance().getReference("VERSION")

    private val platformFlow: Flow<String> = dataStore.data.map {
        it[KEY_PLATFORM] ?: ""
    }.flowOn(dispatcher)

    fun readStoredPlatform(): Flow<String> = platformFlow


    inline fun fetchVersion(
        crossinline isNewVersionExist: (Boolean) -> Unit,
        crossinline onFailure: () -> Unit
    ) {

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newestVersion = snapshot.child("current").value as String

                Log.d("remote", newestVersion)
                Log.d("local", BuildConfig.VERSION_NAME)

                if (newestVersion != BuildConfig.VERSION_NAME) {
                    isNewVersionExist(true) // onTrack
                } else {
                    isNewVersionExist(false)
                }
            }
//                isNewVersionExist(false) // test
//            }

            override fun onCancelled(error: DatabaseError) {
                onFailure()
            }
        })

    }
}
