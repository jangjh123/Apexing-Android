package jyotti.apexing.apexing_android.ui.activity.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.BuildConfig
import jyotti.apexing.apexing_android.base.BaseViewModel
import jyotti.apexing.apexing_android.data.repository.SplashRepositoryV2
import jyotti.apexing.apexing_android.di.IoDispatcher
import jyotti.apexing.apexing_android.di.MainImmediateDispatcher
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect.ShowNewVersionDialog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    init {
        checkVersion()
    }

    private fun checkVersion() {
        repository.fetchVersion().onEach { version ->
            println(version)
            withContext(mainImmediateDispatcher) {
                if (version != BuildConfig.VERSION_NAME) {
                    _effect.emit(ShowNewVersionDialog)
                } else {
                    checkAccount()
                }
            }
        }.catch {
            // todo: 예외처리
        }.launchIn(viewModelScope)
    }

    private fun checkAccount() {

    }
}