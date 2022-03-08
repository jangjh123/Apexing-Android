package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.apexing.apexing_android.BuildConfig.KEY_API
import com.google.gson.JsonArray
import jyotti.apexing.apexing_android.data.local.MatchDao
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import jyotti.apexing.data_store.KEY_REFRESH_DATE
import jyotti.apexing.data_store.KEY_UID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class StatisticsRepository @Inject constructor(
    private val networkManager: NetworkManager,
    private val dataStore: DataStore<Preferences>,
    private val matchDao: MatchDao,
    dispatcher: CoroutineDispatcher
) {

    private val uidFlow: Flow<String> = dataStore.data.map {
        it[KEY_UID] ?: ""
    }.flowOn(dispatcher)

    private val refreshDateFlow: Flow<Long> = dataStore.data.map {
        it[KEY_REFRESH_DATE] ?: 0
    }.flowOn(dispatcher)

    fun readStoredUid() = uidFlow
    fun readStoredRefreshDate() = refreshDateFlow

    fun readMatch(
        uid: String,
        start: Long,
        limit: Int,
        onSuccess: (JsonArray) -> Unit,
        onError: () -> Unit,
        onFailure: () -> Unit
    ) {
        networkManager.getClient().fetchMatch(KEY_API, uid, start, limit).enqueue(object :
            Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                when (response.code()) {
                    200 -> {
                        onSuccess(response.body()!!)
                    }
                    else -> {
                        onError()
                    }
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                onFailure()
            }
        })
    }
}