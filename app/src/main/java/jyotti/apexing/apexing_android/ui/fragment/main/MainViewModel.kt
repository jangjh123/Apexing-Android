package jyotti.apexing.apexing_android.ui.fragment.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.model.main.crafting.Crafting
import jyotti.apexing.apexing_android.data.model.main.map.Map
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.data.model.main.user.User
import jyotti.apexing.apexing_android.data.repository.MainRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: MainRepository,
    dispatcher: CoroutineDispatcher
) : ViewModel() {
    val scope = CoroutineScope(dispatcher)

    private val user = MutableLiveData<User>()
    private val mapList = MutableLiveData<List<Map>>()
    private val craftingList = MutableLiveData<List<Crafting>>()
    private val newsList = MutableLiveData<List<News>>()
    private val notice = MutableLiveData<String>()
    private val contentsCount = MutableLiveData(0)

    fun getUserLiveData() = user
    fun getMapLiveData() = mapList
    fun getCraftingLiveData() = craftingList
    fun getNewsLiveData() = newsList
    fun getNoticeLiveData() = notice
    fun getUser() {
        scope.launch {
            repository.fetchUser(repository.getIdFlow().first()) {
                user.postValue(it)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getMap() {
        repository.fetchGameInfo("MAPS",
            onSuccess = {
                mapList.postValue(it as List<Map>?)
            },
            onFailure = {

            })
    }

    @Suppress("UNCHECKED_CAST")
    fun getCrafting() {
        repository.fetchGameInfo("Craftings",
            onSuccess = {
                craftingList.postValue(it as List<Crafting>)
            },
            onFailure = {

            })
    }

    @Suppress("UNCHECKED_CAST")
    fun getNews() {
        repository.fetchGameInfo("News",
            onSuccess = {
                newsList.postValue(it as List<News>)
            },
            onFailure = {

            })
    }

    fun getNotice() {
        repository.fetchNotice {
            notice.postValue(it)
        }
    }

    inline fun removeAccount(crossinline onFinished: () -> Unit) {
        scope.launch {
            repository.getPlatformFlow().collect { platform ->
                repository.getIdFlow().collect { id ->
                    repository.removeFirebaseUser(platform, id,
                        onSuccess = {
                            scope.launch {
                                repository.clearDataStore()
                            }
                            deleteStoredMatches {
                                onFinished()
                            }
                        })
                }
            }
        }
    }

    inline fun deleteStoredMatches(crossinline onSuccess: () -> Unit) {
        scope.launch {
            withContext(Dispatchers.Default) {
                repository.clearDatabase()
            }
            onSuccess()
        }
    }

//    fun setTimeOut() {
//        scope.launch {
//            delay(5000)
//            timeOutMessage.call()
//        }
//    }
}