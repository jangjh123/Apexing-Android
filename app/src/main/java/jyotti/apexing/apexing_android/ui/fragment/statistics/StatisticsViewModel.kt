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
    private val ratingMessage = SingleLiveEvent<Unit>()

    fun getDatabaseMessage() = databaseMessage
    fun getRatingMessage() = ratingMessage

    fun updateMatch() {
        scope.launch {
            repository.sendMatchRequest(
                id = repository.readStoredId().first(),
                onSuccess = { list ->
//                    when (isForceRefreshing) {
//                        true -> {
//                            scope.launch {
//                                withContext(Dispatchers.IO) {
//                                    repository.clearDatabase()
//                                }
//                                withContext(Dispatchers.IO) {
//                                    repository.storeMatch(list)
//                                }
//                                databaseMessage.call()
//                            }
//                        }
//                        false -> {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            repository.storeMatch(list)
                        }
                        databaseMessage.call()
                    }
//                        }
                },
                onFailure = {

                })
//                    scope.launch {
//                        repository.storeRefreshDate(System.currentTimeMillis() / 1000L)
//                    }
//                },
//                onFailure = {
//                    networkMessage.call()
//                }
//            )
        }
    }

    fun getMatch() = repository.readMatch().cachedIn(viewModelScope)

    fun suggestRating() {
        scope.launch {
            if (!repository.readStoredRatingState().first()) {
                if (Random().nextInt(25) == 10) {
                    ratingMessage.call()
                }
            }
        }
    }

    fun setRatingState() {
        scope.launch {
            repository.storeRatingState()
        }
    }
}