package jyotti.apexing.apexing_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.apexing.apexing_android.BuildConfig.KEY_API
import jyotti.apexing.apexing_android.data.model.main.crafting.Crafting
import jyotti.apexing.apexing_android.data.model.main.map.Maps
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.data.model.main.user.User
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import jyotti.apexing.data_store.KEY_ID
import jyotti.apexing.data_store.KEY_PLATFORM
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val networkManager: NetworkManager,
    private val dataStore: DataStore<Preferences>,
    dispatcher: CoroutineDispatcher
) {
    private val platformFlow = dataStore.data.map {
        it[KEY_PLATFORM] ?: ""
    }.flowOn(dispatcher)

    private val idFlow = dataStore.data.map {
        it[KEY_ID] ?: ""
    }.flowOn(dispatcher)

    fun getPlatformFlow() = platformFlow
    fun getIdFlow() = idFlow

    fun readUser(
        platform: String,
        id: String,
        onSuccess: (User) -> Unit,
        onError: () -> Unit,
        onFailure: () -> Unit
    ) {
        networkManager.getClient().fetchUser(platform, id, KEY_API)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    when (response.code()) {
                        200 -> {
                            onSuccess(response.body()!!)
                        }
                        else -> {
                            onError()
                        }
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    onFailure()
                }
            })
    }

    fun readMap(
        onSuccess: (Maps) -> Unit,
        onError: () -> Unit,
        onFailure: () -> Unit
    ) {
        networkManager.getClient().fetchMap(KEY_API).enqueue(object : Callback<Maps> {
            override fun onResponse(call: Call<Maps>, response: Response<Maps>) {
                when (response.code()) {
                    200 -> {
                        onSuccess(response.body()!!)
                    }
                    else -> {
                        onError()
                    }
                }
            }

            override fun onFailure(call: Call<Maps>, t: Throwable) {
                onFailure()
            }
        })
    }

    fun readCrafting(
        onSuccess: (List<Crafting>) -> Unit,
        onError: () -> Unit,
        onFailure: () -> Unit
    ) {
        networkManager.getClient().fetchCrafting(KEY_API)
            .enqueue(object : Callback<List<Crafting>> {
                override fun onResponse(
                    call: Call<List<Crafting>>,
                    response: Response<List<Crafting>>
                ) {
                    when (response.code()) {
                        200 -> {
                            onSuccess(response.body()!!)
                        }
                        else -> {
                            onError()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Crafting>>, t: Throwable) {
                    onFailure()
                }
            })
    }

    fun readNews(
        onSuccess: (List<News>) -> Unit,
        onError: () -> Unit,
        onFailure: () -> Unit
    ) {
        networkManager.getClient().fetchNews("ko", KEY_API).enqueue(object : Callback<List<News>> {
            override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
                when(response.code()) {
                    200 -> {
                        val newsList = ArrayList<News>()

                        for (i in 0..4) {
                            newsList.add(response.body()!![i])

                            if (i == 4) {
                                onSuccess(newsList)
                            }
                        }

                    } else -> {
                        onError()
                    }
                }
            }

            override fun onFailure(call: Call<List<News>>, t: Throwable) {
                onFailure()
            }
        })
    }

    suspend fun clearDataStore() {
        dataStore.edit {
            it.clear()
        }
    }
}