package jyotti.apexing.apexing_android.ui.fragment.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.repository.StatisticsRepository
import jyotti.apexing.apexing_android.util.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: StatisticsRepository,
    dispatcher: CoroutineDispatcher
) :
    ViewModel() {
    private val scope = CoroutineScope(dispatcher)
    private val networkMessage = SingleLiveEvent<Unit>()
    private val databaseMessage = SingleLiveEvent<Unit>()
    private val timeOutMessage = SingleLiveEvent<Unit>()
    private val ratingMessage = SingleLiveEvent<Unit>()

    fun getNetworkMessage() = networkMessage
    fun getDatabaseMessage() = databaseMessage
    fun getTimeOutMessage() = timeOutMessage
    fun getRatingMessage() = ratingMessage

    fun updateMatch(isForceRefreshing: Boolean) {
        scope.launch {
            repository.sendMatchRequest(
                repository.readStoredUid().first(),
                when (isForceRefreshing) {
                    true -> {
                        0
                    }
                    false -> {
                        repository.readStoredRefreshDate().first()
                    }
                },
                onSuccess = { list ->
                    scope.launch {
                        repository.storeRefreshDate(System.currentTimeMillis() / 1000L)
                        withContext(Dispatchers.Default) {
                            when (isForceRefreshing) {
                                true -> {
                                    repository.clearDatabase()
                                    repository.storeMatch(list)
                                }
                                false -> {
                                    repository.storeMatch(list)
                                }
                            }
                        }
                        databaseMessage.call()
                    }
                },
                onError = {
                    updateMatch(isForceRefreshing)
                },
                onFailure = {
                    networkMessage.call()
                }

            )
        }
    }

    fun getMatch() = repository.readMatch().cachedIn(viewModelScope)

    fun setTimeOut() {
        scope.launch {
            delay(5000)
            timeOutMessage.call()
        }
    }

    fun suggestRating() {
        if (Random().nextInt(25) == 10) {
            ratingMessage.call()
        }
    }
}