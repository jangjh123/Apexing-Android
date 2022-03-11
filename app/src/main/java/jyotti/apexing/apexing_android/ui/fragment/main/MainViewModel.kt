package jyotti.apexing.apexing_android.ui.fragment.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.model.main.crafting.Crafting
import jyotti.apexing.apexing_android.data.model.main.map.Map
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.data.model.main.user.User
import jyotti.apexing.apexing_android.data.repository.MainRepository
import jyotti.apexing.apexing_android.util.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val scope = CoroutineScope(dispatcher)

    private val user = MutableLiveData<User>()
    private val mapList = MutableLiveData<List<Map>>()
    private val craftingList = MutableLiveData<List<Crafting>>()
    private val newsList = MutableLiveData<List<News>>()
    private val contentsCount = MutableLiveData(0)
    private val networkMessage = SingleLiveEvent<Unit>()
    private val timeOut = SingleLiveEvent<Unit>()

    fun getUserLiveData() = user
    fun getMapLiveData() = mapList
    fun getCraftingLiveData() = craftingList
    fun getNewsLiveData() = newsList
    fun getContentsCount() = contentsCount
    fun getNetworkMessage() = networkMessage
    fun getTimeOut() = timeOut

    fun getUser() {
        scope.launch {
            repository.sendUserRequest(
                repository.getPlatformFlow().first(),
                repository.getIdFlow().first(),
                onSuccess = {
                    user.postValue(it)
                },
                onError = {
                    getUser()
                },
                onFailure = {
                    networkMessage.call()
                })
        }
    }

    fun getMap() {
        repository.sendMapRequest(
            onSuccess = {
                val list = listOf(it.battleRoyal, it.ranked, it.arenas, it.arenasRanked)
                mapList.postValue(list)
            },
            onError = {
                getMap()
            },
            onFailure = {
                networkMessage.call()
            }
        )
    }

    fun getCrafting() {
        repository.sendCraftingRequest(
            onSuccess = {
                craftingList.postValue(it)
            },
            onError = {
                getCrafting()
            },
            onFailure = {
                networkMessage.call()
            }
        )
    }

    fun getNews() {
        repository.sendNewsRequest(
            onSuccess = {
                newsList.postValue(it)
            },
            onError = {
                getNews()
            },
            onFailure = {
                networkMessage.call()
            }
        )
    }

    fun addContentsCount() {
        contentsCount.postValue(contentsCount.value?.plus(1))
    }

    fun removeAccount() {
        scope.launch {
            repository.clearDataStore()
        }
    }

    fun setTimeOut() {
        scope.launch {
            delay(10000)
            timeOut.call()
        }
    }
}