package jyotti.apexing.apexing_android.ui.fragment.statistics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.base.BaseViewModel
import jyotti.apexing.apexing_android.data.repository.StatisticsRepository
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity.Companion.KEY_ID
import jyotti.apexing.apexing_android.ui.fragment.statistics.StatisticsUiContract.UiEffect
import jyotti.apexing.apexing_android.ui.fragment.statistics.StatisticsUiContract.UiEffect.ScrollToTop
import jyotti.apexing.apexing_android.ui.fragment.statistics.StatisticsUiContract.UiState
import jyotti.apexing.apexing_android.util.getCoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel, StatisticsUiContract, ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    private val coroutineExceptionHandler = getCoroutineExceptionHandler(
        onUnknownHostException = {
            viewModelScope.launch {

            }
        }
    )

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update {
                it.copy(isLoading = true)
            }

            getStatistics()

            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private suspend fun getStatistics() {
        savedStateHandle.get<String>(KEY_ID)?.let { id ->
            _uiState.update {
                it.copy(statistics = statisticsRepository.fetchStatistics(id))
            }
        }
    }

    fun onClickFloatingActionButton() {
        viewModelScope.launch {
            _uiEffect.emit(ScrollToTop)
        }
    }
}