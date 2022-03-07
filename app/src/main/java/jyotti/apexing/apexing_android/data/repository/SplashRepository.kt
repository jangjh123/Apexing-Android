package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import jyotti.apexing.datastore.KEY_PLATFORM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SplashRepository @Inject constructor(datastore: DataStore<Preferences>) {

    private val platformFlow: Flow<String> = datastore.data.map { preferences ->
        preferences[KEY_PLATFORM] ?: ""
    }.flowOn(Dispatchers.IO)

    fun readSavedPlatform(): Flow<String> = platformFlow
}
