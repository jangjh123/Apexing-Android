package jyotti.apexing.apexing_android.ui.activity.splash

import jyotti.apexing.apexing_android.base.BaseContract
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiState

interface SplashUiContract : BaseContract<UiState, UiEffect> {
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