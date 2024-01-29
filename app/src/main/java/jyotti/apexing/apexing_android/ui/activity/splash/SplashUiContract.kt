package jyotti.apexing.apexing_android.ui.activity.splash

import jyotti.apexing.apexing_android.base.BaseContract

interface SplashUiContract : BaseContract<SplashUiContract.UiState, SplashUiContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false
    )

    sealed interface UiEffect {
        object GoToAccount : UiEffect

        data class GoToHome(val id: String) : UiEffect

        object ShowNewVersionDialog : UiEffect

        data class ShowErrorDialog(val stringId: Int) : UiEffect
    }
}