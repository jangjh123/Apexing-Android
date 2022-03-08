package jyotti.apexing.apexing_android.ui.fragment.statistics

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.repository.StatisticsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: StatisticsRepository,
    dispatcher: CoroutineDispatcher
) :
    ViewModel() {

    private val scope = CoroutineScope(dispatcher)

    fun updateMatch() {
        scope.launch {
            repository.sendMatchRequest(
                repository.readStoredUid().first(),
                repository.readStoredRefreshDate().first(),
                onSuccess = {

                },
                onError = {

                },
                onFailure = {

                }

            )
        }

    }
}