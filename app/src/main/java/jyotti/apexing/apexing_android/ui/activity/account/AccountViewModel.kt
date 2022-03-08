package jyotti.apexing.apexing_android.ui.activity.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.repository.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository,
    dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val scope = CoroutineScope(dispatcher)
    private val message = MutableLiveData<Int>()

    fun getMessageLiveData() = message

    fun checkAccount(platform: String, id: String) {
        repository.sendAccountRequest(platform, id,
            onSuccess = {
                message.postValue(0)
                scope.launch {
                    repository.storeAccount(platform, id, it)
                }
            },
            onNull = {
                message.postValue(1)
            },
            onError = {
                message.postValue(2)
            },
            onFailure = {
                message.postValue(3)
            })
    }
}
