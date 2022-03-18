package jyotti.apexing.apexing_android.ui.activity.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseActivity
import jyotti.apexing.apexing_android.ui.activity.account.AccountActivity
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels()

    override fun startProcess() {
        checkVersion()
    }

    private fun checkAccount() {
        viewModel.getStoredPlatform()
    }

    private fun checkVersion() {
        viewModel.getNewVersionCode()
    }

    override fun setObservers() {
        viewModel.getVersionLiveData().observe(this) {
            if (it) {

            } else {
                checkAccount()
            }
        }

        viewModel.getNetworkMessage().observe(this) {

        }

        viewModel.getPlatformLiveData().observe(this) {
            val intent: Intent = if (it.isEmpty()) {
                Intent(this, AccountActivity::class.java)
            } else {
                Intent(this, HomeActivity::class.java)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}