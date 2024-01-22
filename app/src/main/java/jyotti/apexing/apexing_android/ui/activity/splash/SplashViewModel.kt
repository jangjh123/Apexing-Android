package jyotti.apexing.apexing_android.ui.activity.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.BuildConfig
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseViewModel
import jyotti.apexing.apexing_android.data.repository.SplashRepository
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
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    private val repository: SplashRepository
) : BaseViewModel, SplashUiContract, ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState

    private val _uiEffect = MutableSharedFlow<UiEffect>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
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
        viewModelScope.launch(coroutineExceptionHandler) {
            val version = repository.fetchVersion()

            if (version != BuildConfig.VERSION_NAME) {
                _uiEffect.emit(ShowNewVersionDialog)
            } else {
                checkAccount()
            }
        }
    }

    private fun checkAccount() {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            val storedId = repository.readStoredId()

            withContext(mainImmediateDispatcher) {
                storedId?.let {
                    checkDormancy(storedId)
                } ?: run {
                    _uiEffect.emit(GoToAccountActivity)
                }
            }
        }
    }

    private fun checkDormancy(storedId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val isDormancy = repository.fetchIsDormancy(storedId)

            if (isDormancy) {
                _uiEffect.emit(GoToAccountActivity)
            } else {
                updateLastConnectedTime(storedId)
            }
        }
    }

    private suspend fun updateLastConnectedTime(storedId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.fetchLastConnectedTime(storedId)
            _uiEffect.emit(GoToMainActivity(storedId))
        }
    }
}