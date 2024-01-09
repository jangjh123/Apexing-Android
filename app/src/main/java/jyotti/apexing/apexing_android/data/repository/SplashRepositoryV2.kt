package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject

class SplashRepositoryV2 @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
}