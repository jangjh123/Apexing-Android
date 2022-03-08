package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import jyotti.apexing.apexing_android.data.local.MatchDao
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import javax.inject.Inject

class StatisticsRepository @Inject constructor(
    private val networkManager: NetworkManager,
    private val dataStore: DataStore<Preferences>,
    private val matchDao: MatchDao
) {
}