package jyotti.apexing.apexing_android.ui.fragment.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.base.BaseViewModel
import jyotti.apexing.apexing_android.data.repository.MainRepositoryV2
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivityV2
import jyotti.apexing.apexing_android.ui.fragment.main.MainUiContract.UiEffect
import jyotti.apexing.apexing_android.ui.fragment.main.MainUiContract.UiState
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
class MainViewModelV2 @Inject constructor(
    private val mainRepository: MainRepositoryV2,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel, MainUiContract, ViewModel() {
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
        getMaps()
        getNotice()
        getCraftings()
        getUserInfo()
        getNewses()
    }

    private fun getMaps() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update {
                it.copy(maps = mainRepository.fetchMaps())
            }
        }
    }

    private fun getNotice() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update {
                it.copy(notice = mainRepository.fetchNotice())
            }
        }
    }

    private fun getCraftings() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update {
                it.copy(craftings = mainRepository.fetchCraftings())
            }
        }
    }

    private fun getUserInfo() {
        savedStateHandle.get<String>(HomeActivityV2.KEY_ID)?.let { id ->
            viewModelScope.launch(coroutineExceptionHandler) {
                _uiState.update {
                    it.copy(userInfo = mainRepository.fetchUserInfo(id))
                }
            }
        }
    }

    private fun getNewses() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update {
                it.copy(newses = mainRepository.fetchNewses())
            }
        }
    }
}