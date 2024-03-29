package jyotti.apexing.apexing_android.ui.activity.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseViewModel
import jyotti.apexing.apexing_android.data.remote.ApexLegendsApiError
import jyotti.apexing.apexing_android.data.repository.AccountRepository
import jyotti.apexing.apexing_android.di.IoDispatcher
import jyotti.apexing.apexing_android.ui.activity.account.AccountUiContract.UiEffect
import jyotti.apexing.apexing_android.ui.activity.account.AccountUiContract.UiEffect.GoToHelpPage
import jyotti.apexing.apexing_android.ui.activity.account.AccountUiContract.UiEffect.GoToHome
import jyotti.apexing.apexing_android.ui.activity.account.AccountUiContract.UiEffect.ShowSnackBar
import jyotti.apexing.apexing_android.ui.activity.account.AccountUiContract.UiState
import jyotti.apexing.apexing_android.util.getCoroutineExceptionHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: AccountRepository
) : BaseViewModel, AccountUiContract, ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    private val coroutineExceptionHandler = getCoroutineExceptionHandler(
        onUnknownHostException = {
            _uiEffect.emit(ShowSnackBar(R.string.exception_network))
        },
        onElse = { throwable ->
            when (throwable) {
                is ApexLegendsApiError.SlowDown -> {
                    getOriginAccount()
                }

                is ApexLegendsApiError.PlayerNotFound -> {
                    _uiEffect.emit(ShowSnackBar(R.string.exception_player_not_found))
                }

                is ApexLegendsApiError.Unknown -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                    _uiEffect.emit(ShowSnackBar(R.string.exception_unknown))
                }
            }
        }
    )

    fun onClickHelp() {
        viewModelScope.launch {
            _uiEffect.emit(GoToHelpPage)
        }
    }

    fun onIdTextChanged(
        idText: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        _uiState.update {
            it.copy(idText = idText.toString())
        }
    }

    fun onClickEnroll() {
        viewModelScope.launch {
            val id = uiState.value.idText
            if (id.isNotEmpty()) {
                _uiState.update {
                    it.copy(isLoading = true)
                }

                getOriginAccount()
            } else {
                _uiEffect.emit(ShowSnackBar(R.string.please_set_id))
            }
        }
    }

    private fun getOriginAccount() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val id = uiState.value.idText
            val uid = repository.fetchOriginAccount(id)

            withContext(ioDispatcher) {
                repository.storeAccount(
                    id = id,
                    uid = uid
                )
            }

            _uiEffect.emit(GoToHome(id))
        }
    }
}