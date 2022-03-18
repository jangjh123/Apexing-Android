package jyotti.apexing.apexing_android.ui.activity.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.repository.SplashRepository
import jyotti.apexing.apexing_android.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: SplashRepository,
    dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val scope = CoroutineScope(dispatcher)
    private val platform = MutableLiveData<String>()
    private val version = MutableLiveData<Boolean>()
    private val networkMessage = SingleLiveEvent<Unit>()

    fun getPlatformLiveData() = platform
    fun getVersionLiveData() = version
    fun getNetworkMessage() = networkMessage

    fun getStoredPlatform() {
        scope.launch {
            platform.postValue(repository.readStoredPlatform().first())
        }
    }

    fun getNewVersionCode() {
        repository.fetchVersion {
            version.postValue(it)
        }
    }
}