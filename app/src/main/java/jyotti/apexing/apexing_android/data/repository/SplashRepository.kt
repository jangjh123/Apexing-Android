package jyotti.apexing.apexing_android.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
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

    private val platformFlow: Flow<String> = dataStore.data.map {
        it[KEY_PLATFORM] ?: ""
    }.flowOn(dispatcher)

    fun readStoredPlatform(): Flow<String> = platformFlow

    inline fun fetchVersion(
        crossinline onSuccess: (Boolean) -> Unit
    ) {
        val remoteConfig = Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings
            {
                minimumFetchIntervalInSeconds = 0
            })
            setDefaultsAsync(mapOf("REMOTE_KEY_APP_VERSION" to 0))
        }

        remoteConfig.fetchAndActivate().addOnSuccessListener {
            if (remoteConfig.getString("current_version") != BuildConfig.VERSION_NAME) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }
}
