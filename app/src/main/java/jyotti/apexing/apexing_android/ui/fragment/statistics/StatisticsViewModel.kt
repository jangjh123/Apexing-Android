package jyotti.apexing.apexing_android.ui.fragment.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.repository.StatisticsRepository
import jyotti.apexing.apexing_android.util.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: StatisticsRepository,
    dispatcher: CoroutineDispatcher
) :
    ViewModel() {
    private val scope = CoroutineScope(dispatcher)
    private val databaseMessage = SingleLiveEvent<Unit>()
    private val timeOut = SingleLiveEvent<Unit>()

    fun getDatabaseMessage() = databaseMessage
    fun getTimeOut() = timeOut

    fun updateMatch() {
        scope.launch {
            repository.sendMatchRequest(
                repository.readStoredUid().first(),
                repository.readStoredRefreshDate().first(),
                onSuccess = {
                    scope.launch {
                        repository.storeRefreshDate(System.currentTimeMillis() / 1000L)
                        withContext(Dispatchers.Default) {
                            repository.storeMatch(it)
                        }

                        databaseMessage.call()
                    }
                },
                onError = {
                    updateMatch()
                },
                onFailure = {

                }

            )
        }
    }

    fun getMatch() = repository.readMatch().cachedIn(viewModelScope)

    fun setTimeOut() {
        scope.launch {
            delay(5000)
            timeOut.call()
        }
    }
}