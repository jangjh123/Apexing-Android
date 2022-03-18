package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
}
