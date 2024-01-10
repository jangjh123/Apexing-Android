package jyotti.apexing.apexing_android.ui.activity.splash

import android.annotation.SuppressLint
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseActivityV2
import jyotti.apexing.apexing_android.databinding.ActivitySplashBinding
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect.GoToAccountActivity
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect.GoToMainActivity
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect.ShowErrorDialog
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.SplashUiEffect.ShowNewVersionDialog
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivityV2 : BaseActivityV2<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override val viewModel: SplashViewModelV2 by viewModels()

    override fun initBinding() {
        bind {
            vm = viewModel
        }
    }

    override fun collectUiEffect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is GoToAccountActivity -> {

                    }

                    is GoToMainActivity -> {

                    }

                    is ShowNewVersionDialog -> {

                    }

                    is ShowErrorDialog -> {

                    }
                }
            }
        }
    }
}