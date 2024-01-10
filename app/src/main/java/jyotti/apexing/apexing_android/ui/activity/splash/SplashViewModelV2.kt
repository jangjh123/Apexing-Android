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
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect.GoToAccountActivity
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect.GoToMainActivity
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect.ShowErrorDialog
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect.ShowNewVersionDialog
import jyotti.apexing.apexing_android.util.getCoroutineExceptionHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
) : BaseViewModel, ViewModel() {
    private val _effect = MutableSharedFlow<SplashUiEffect>()
    val effect = _effect.asSharedFlow()

    private val coroutineExceptionHandler = getCoroutineExceptionHandler(
        onUnknownHostException = {
            viewModelScope.launch {
                _effect.emit(ShowErrorDialog(R.string.exception_network))
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
                    _effect.emit(ShowNewVersionDialog)
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
                    _effect.emit(GoToAccountActivity)
                }
            }
        }.launchIn(viewModelScope + ioDispatcher + coroutineExceptionHandler)
    }

    private fun checkDormancy(storedId: String) {
        repository.fetchIsDormancy(storedId).onEach { isDormancy ->
            withContext(mainImmediateDispatcher) {
                if (isDormancy == true) {
                    _effect.emit(GoToAccountActivity)
                } else {
                    updateLastConnectedTime(storedId)
                }
            }
        }.launchIn(viewModelScope + ioDispatcher + coroutineExceptionHandler)
    }

    private suspend fun updateLastConnectedTime(storedId: String) {
        withContext(ioDispatcher) {
            repository.fetchLastConnectedTime(storedId).collect {
                _effect.emit(GoToMainActivity(storedId))
            }
        }
    }
}