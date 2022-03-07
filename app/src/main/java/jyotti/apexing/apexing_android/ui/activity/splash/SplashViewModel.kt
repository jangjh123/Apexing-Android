package jyotti.apexing.apexing_android.ui.activity.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.repository.SplashRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: SplashRepository,
    dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val scope = CoroutineScope(dispatcher)
    private val platformData = MutableLiveData<String>()

    fun getStoredPlatform() {
        scope.launch {
            repository.readSavedPlatform().collect {
                platformData.postValue(it)
            }
        }
    }

    fun getPlatformLiveData() = platformData
}