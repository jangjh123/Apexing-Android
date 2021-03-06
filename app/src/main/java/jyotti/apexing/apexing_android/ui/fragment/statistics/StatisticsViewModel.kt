package jyotti.apexing.apexing_android.ui.fragment.statistics

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.model.statistics.RefreshIndex
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
    private val _refreshIndexLiveData = MutableLiveData<RefreshIndex>()
    val refreshIndexLiveData: LiveData<RefreshIndex>
        get() = _refreshIndexLiveData
    private val databaseMessage = SingleLiveEvent<Unit>()
    private val ratingMessage = SingleLiveEvent<Unit>()
    private val _noElementLiveData = MutableLiveData<Pair<Int, Int>>()
    val noElementLiveData: LiveData<Pair<Int,Int>>
        get() = _noElementLiveData

    fun getDatabaseMessage() = databaseMessage
    fun getRatingMessage() = ratingMessage

    @SuppressLint("NullSafeMutableLiveData")
    fun updateMatch() {
        scope.launch {
            repository.sendMatchRequest(
                id = repository.readStoredId().first(),
                onSuccess = { pair ->
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            repository.storeMatch(pair.first)
                        }
                        databaseMessage.call()
                        _refreshIndexLiveData.postValue(pair.second)
                    }
                },
                onComplete = {
                    databaseMessage.call()
                    _refreshIndexLiveData.postValue(it)
                },
                onNoElement = {
                    _noElementLiveData.postValue(it)
                },
                onFailure = {

                })
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