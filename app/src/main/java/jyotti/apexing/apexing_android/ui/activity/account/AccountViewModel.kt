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
    private val message = MutableLiveData<AccountMessage>()

    fun getMessageLiveData() = message

    fun checkAccount(id: String, platform: String) {
        repository.sendAccountRequest(platform, id,
            onSuccess = {
                message.postValue(AccountMessage.Success)
                scope.launch {
                    repository.storeAccount(platform, id, it)
                }
            },
            onNull = {
                message.postValue(AccountMessage.Null)
            },
            onError = {
                checkAccount(platform, id)
            },
            onFailure = {
                message.postValue(AccountMessage.NetworkError)
            })
    }
}

sealed class AccountMessage {
    object Success : AccountMessage()
    object Null : AccountMessage()
    object Error : AccountMessage()
    object NetworkError : AccountMessage()
}
