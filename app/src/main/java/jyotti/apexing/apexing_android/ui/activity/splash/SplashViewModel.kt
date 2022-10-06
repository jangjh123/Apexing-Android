package jyotti.apexing.apexing_android.ui.activity.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.repository.SplashRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: SplashRepository,
    dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val scope = CoroutineScope(dispatcher)
    private val _nextScreenInfo = MutableLiveData<String>()
    val nextScreenInfo: LiveData<String>
        get() = _nextScreenInfo

    init {
        setNextScreenInfo()
    }

    private fun setNextScreenInfo() {
        checkNewVersionExist()
    }

    private fun checkNewVersionExist() {
        scope.launch {
            repository.fetchVersion(
                onComplete = {
                    when (it) {
                        true -> {
                            _nextScreenInfo.postValue("newVersion")
                        }
                        false -> {
                            checkAccountExist()
                        }
                    }
                },
                onFailure = {
                    _nextScreenInfo.postValue("error")
                }
            )
        }
    }

    private fun checkAccountExist() {
        scope.launch {
            val storedId = withContext(scope.coroutineContext) {
                repository.getStoredIdFlow().first()
            }

            if (storedId.isEmpty()) {
                _nextScreenInfo.postValue("account")
            } else {
                checkDormancy()
            }
        }
    }


    private fun checkDormancy() {
        scope.launch {
            val storedId = withContext(scope.coroutineContext) {
                repository.getStoredIdFlow().first()
            }

            repository.fetchDormancy(
                storedId,
                onSuccess = {
                    when (it) {
                        true -> {
                            _nextScreenInfo.postValue("account")
                        }
                        false -> {
                            _nextScreenInfo.postValue("home")
                        }
                    }
                },
                onFailure = {
                    _nextScreenInfo.postValue("error")
                })
        }
    }
}
