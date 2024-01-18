package jyotti.apexing.apexing_android.ui.activity.splash

import android.annotation.SuppressLint
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseActivityV2
import jyotti.apexing.apexing_android.databinding.ActivitySplashBinding
import jyotti.apexing.apexing_android.ui.activity.account.AccountActivity
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.GoToAccountActivity
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.GoToMainActivity
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.ShowErrorDialog
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.ShowNewVersionDialog
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivityV2<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override val viewModel: SplashViewModel by viewModels()

    override fun initBinding() {
        bind {
            vm = viewModel
        }
    }

    override fun collectUiEffect() {
        repeatCallDefaultOnStarted {
            viewModel.uiEffect.collectLatest { effect ->
                when (effect) {
                    is GoToAccountActivity -> {
                        goToAccountActivity()
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

    private fun goToAccountActivity() {
        startActivity(AccountActivity.newIntent(this@SplashActivity))
    }
}