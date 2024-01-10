package jyotti.apexing.apexing_android.ui.activity.splash

interface SplashUiContract {
    sealed interface SplashUiEffect {
        object GoToAccountActivity : SplashUiEffect

        object GoToMainActivity : SplashUiEffect

        object ShowNewVersionDialog : SplashUiEffect

        object ShowErrorDialog : SplashUiEffect
    }
}