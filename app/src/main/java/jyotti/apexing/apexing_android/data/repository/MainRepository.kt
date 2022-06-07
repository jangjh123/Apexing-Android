package jyotti.apexing.apexing_android.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.database.*
import jyotti.apexing.apexing_android.BuildConfig.KEY_API
import jyotti.apexing.apexing_android.data.local.MatchDao
import jyotti.apexing.apexing_android.data.model.main.crafting.Crafting
import jyotti.apexing.apexing_android.data.model.main.map.Map
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.data.model.main.user.User
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import jyotti.apexing.data_store.KEY_ID
import jyotti.apexing.data_store.KEY_PLATFORM
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    val networkManager: NetworkManager,
    val databaseRef: DatabaseReference,
    private val dataStore: DataStore<Preferences>,
    private val matchDao: MatchDao,
    dispatcher: CoroutineDispatcher
) {
    val reference = FirebaseDatabase.getInstance()

    private val platformFlow = dataStore.data.map {
        it[KEY_PLATFORM] ?: ""
    }.flowOn(dispatcher)

    private val idFlow = dataStore.data.map {
        it[KEY_ID] ?: ""
    }.flowOn(dispatcher)

    fun getPlatformFlow() = platformFlow
    fun getIdFlow() = idFlow

    inline fun fetchGameInfo(
        child: String,
        crossinline onSuccess: (List<Any>) -> Unit,
        crossinline onFailure: () -> Unit
    ) {
        reference.getReference(child).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Any>()
                when (child) {
                    "Craftings" -> {
                        list.add(
                            Crafting(
                                snapshot.child("Daily_0").child("asset").value.toString(),
                                snapshot.child("Daily_0").child("cost").value.toString()
                            )
                        )
                        list.add(
                            Crafting(
                                snapshot.child("Daily_1").child("asset").value.toString(),
                                snapshot.child("Daily_1").child("cost").value.toString()
                            )
                        )
                        list.add(
                            Crafting(
                                snapshot.child("Weekly_0").child("asset").value.toString(),
                                snapshot.child("Weekly_0").child("cost").value.toString()
                            )
                        )
                        list.add(
                            Crafting(
                                snapshot.child("Weekly_1").child("asset").value.toString(),
                                snapshot.child("Weekly_1").child("cost").value.toString()
                            )
                        )
                        onSuccess(list)
                    }
                    "MAPS" -> {
                        list.add(
                            Map(
                                type = "배틀로얄 (노말)",
                                snapshot.child("BR_normal").child("asset").value.toString(),
                                snapshot.child("BR_normal").child("map").value.toString(),
                                snapshot.child("BR_normal").child("end").value.toString()
                            )
                        )
                        list.add(
                            Map(
                                "배틀로얄 (랭크)",
                                snapshot.child("BR_rank").child("asset").value.toString(),
                                snapshot.child("BR_rank").child("map").value.toString(),
                                snapshot.child("BR_rank").child("end").value.toString()
                            )
                        )
                        list.add(
                            Map(
                                "아레나 (노말)",
                                snapshot.child("AR_normal").child("asset").value.toString(),
                                snapshot.child("AR_normal").child("map").value.toString(),
                                snapshot.child("AR_normal").child("end").value.toString()
                            )
                        )
                        list.add(
                            Map(
                                "아레나 (랭크)",
                                snapshot.child("AR_rank").child("asset").value.toString(),
                                snapshot.child("AR_rank").child("map").value.toString(),
                                snapshot.child("AR_rank").child("end").value.toString()
                            )
                        )
                        onSuccess(list)
                    }
                    "News" -> {
                        list.add(
                            News(
                                snapshot.child("News_0").child("title").value.toString(),
                                snapshot.child("News_0").child("link").value.toString(),
                                snapshot.child("News_0").child("img").value.toString(),
                                snapshot.child("News_0").child("short_desc").value.toString()
                            )
                        )
                        list.add(
                            News(
                                snapshot.child("News_1").child("title").value.toString(),
                                snapshot.child("News_1").child("link").value.toString(),
                                snapshot.child("News_1").child("img").value.toString(),
                                snapshot.child("News_1").child("short_desc").value.toString()
                            )
                        )
                        list.add(
                            News(
                                snapshot.child("News_2").child("title").value.toString(),
                                snapshot.child("News_2").child("link").value.toString(),
                                snapshot.child("News_2").child("img").value.toString(),
                                snapshot.child("News_2").child("short_desc").value.toString()
                            )
                        )
                        list.add(
                            News(
                                snapshot.child("News_3").child("title").value.toString(),
                                snapshot.child("News_3").child("link").value.toString(),
                                snapshot.child("News_3").child("img").value.toString(),
                                snapshot.child("News_3").child("short_desc").value.toString()
                            )
                        )
                        list.add(
                            News(
                                snapshot.child("News_4").child("title").value.toString(),
                                snapshot.child("News_4").child("link").value.toString(),
                                snapshot.child("News_4").child("img").value.toString(),
                                snapshot.child("News_4").child("short_desc").value.toString()
                            )
                        )
                        onSuccess(list)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure()
            }
        })
    }

    inline fun sendUserRequest(
        platform: String,
        id: String,
        crossinline onSuccess: (User) -> Unit,
        crossinline onError: () -> Unit
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
                    onError()
                    Log.d("onFailure", t.toString())
                }
            })
    }

    suspend fun clearDatabase() {
        matchDao.deleteAll()
    }

    suspend fun clearDataStore() {
        dataStore.edit {
            it.clear()
        }
    }

    inline fun removeFirebaseUser(
        platform: String,
        id: String,
        crossinline onSuccess: () -> Unit
    ) {
        databaseRef.child(platform).orderByKey().equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.child(id).removeValue()
                onSuccess()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}