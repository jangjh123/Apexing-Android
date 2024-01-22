package jyotti.apexing.apexing_android.ui.activity.account

import jyotti.apexing.apexing_android.base.BaseContract

interface AccountUiContract : BaseContract<AccountUiContract.UiState, AccountUiContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false,
        val idText: String = ""
    )

    sealed interface UiEffect {
        object GoToHelpPage : UiEffect

        data class ShowSnackBar(val stringId: Int) : UiEffect

        data class GoToHome(val id: String) : UiEffect
    }
}