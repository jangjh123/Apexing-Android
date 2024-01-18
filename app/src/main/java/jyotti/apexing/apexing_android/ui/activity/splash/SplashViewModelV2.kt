package jyotti.apexing.apexing_android.ui.activity.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.BuildConfig
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseViewModel
import jyotti.apexing.apexing_android.data.repository.SplashRepositoryV2
import jyotti.apexing.apexing_android.di.IoDispatcher
import jyotti.apexing.apexing_android.di.MainImmediateDispatcher
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.GoToAccountActivity
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.GoToMainActivity
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.ShowErrorDialog
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.ShowNewVersionDialog
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiState
import jyotti.apexing.apexing_android.util.getCoroutineExceptionHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModelV2 @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    private val repository: SplashRepositoryV2
) : BaseViewModel, SplashUiContract, ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect

    private val coroutineExceptionHandler = getCoroutineExceptionHandler(
        onUnknownHostException = {
            viewModelScope.launch {
                _uiEffect.emit(ShowErrorDialog(R.string.exception_network))
            }
        }
    )

    init {
        checkVersion()
    }

    private fun checkVersion() {
        repository.fetchVersion().onEach { version ->
            withContext(mainImmediateDispatcher) {
                if (version != BuildConfig.VERSION_NAME) {
                    _uiEffect.emit(ShowNewVersionDialog)
                } else {
                    checkAccount()
                }
            }
        }.launchIn(viewModelScope + ioDispatcher + coroutineExceptionHandler)
    }

    private fun checkAccount() {
        repository.readStoredId().onEach { storedId ->
            withContext(mainImmediateDispatcher) {
                if (storedId != null) {
                    checkDormancy(storedId)
                } else {
                    _uiEffect.emit(GoToAccountActivity)
                }
            }
        }.launchIn(viewModelScope + ioDispatcher + coroutineExceptionHandler)
    }

    private fun checkDormancy(storedId: String) {
        repository.fetchIsDormancy(storedId).onEach { isDormancy ->
            withContext(mainImmediateDispatcher) {
                if (isDormancy == true) {
                    _uiEffect.emit(GoToAccountActivity)
                } else {
                    updateLastConnectedTime(storedId)
                }
            }
        }.launchIn(viewModelScope + ioDispatcher + coroutineExceptionHandler)
    }

    private suspend fun updateLastConnectedTime(storedId: String) {
        withContext(ioDispatcher) {
            repository.fetchLastConnectedTime(storedId).collect {
                _uiEffect.emit(GoToMainActivity(storedId))
            }
        }
    }
}