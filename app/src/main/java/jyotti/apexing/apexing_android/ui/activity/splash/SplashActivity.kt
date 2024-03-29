package jyotti.apexing.apexing_android.ui.activity.splash

import android.annotation.SuppressLint
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseActivity
import jyotti.apexing.apexing_android.databinding.ActivitySplashBinding
import jyotti.apexing.apexing_android.ui.activity.account.AccountActivity
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.GoToAccount
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.GoToHome
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.ShowErrorDialog
import jyotti.apexing.apexing_android.ui.activity.splash.SplashUiContract.UiEffect.ShowNewVersionDialog
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override val viewModel: SplashViewModel by viewModels()

    override fun initBinding() {
        bind {
            vm = viewModel
        }
    }

    override fun collectUiState() = Unit

    override fun collectUiEffect() {
        repeatCallDefaultOnStarted {
            viewModel.uiEffect.collect { uiEffect ->
                when (uiEffect) {
                    is GoToAccount -> goToAccount()

                    is GoToHome -> goToHome(uiEffect.id)

                    is ShowNewVersionDialog -> Unit

                    is ShowErrorDialog -> Unit
                }
            }
        }
    }

    private fun goToAccount() {
        startActivity(AccountActivity.newIntent(this@SplashActivity))
        finish()
    }

    private fun goToHome(id: String) {
        startActivity(
            HomeActivity.newIntent(
                context = this@SplashActivity,
                id = id,
            )
        )
        finish()
    }
}